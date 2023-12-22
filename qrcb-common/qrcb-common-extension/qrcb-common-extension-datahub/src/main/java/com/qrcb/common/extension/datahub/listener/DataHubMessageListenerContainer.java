package com.qrcb.common.extension.datahub.listener;

import com.aliyun.datahub.client.exception.*;
import com.aliyun.datahub.client.model.RecordEntry;
import com.aliyun.datahub.clientlibrary.config.ConsumerConfig;
import com.aliyun.datahub.clientlibrary.consumer.Consumer;
import com.qrcb.common.extension.datahub.config.DataHubListenerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@Slf4j
public class DataHubMessageListenerContainer extends AbstractMessageListenerContainer {
    private static final String UNUSED = "unused";

    private static final int DEFAULT_ACK_TIME = 5000;
    private Runnable emergencyStop = () -> stopAbnormally( () -> {
        // NOSONAR
    } );
    private final AbstractMessageListenerContainer thisOrParentContainer;

    private volatile ListenerConsumer listenerConsumer;

    private volatile ListenableFuture<?> listenerConsumerFuture;

    private volatile CountDownLatch startLatch = new CountDownLatch( 1 );

    protected DataHubMessageListenerContainer(ContainerProperties containerProperties) {
        super( containerProperties );
        this.thisOrParentContainer = this;
    }

    public void setEmergencyStop(Runnable emergencyStop) {
        Assert.notNull( emergencyStop, "'emergencyStop' cannot be null" );
        this.emergencyStop = emergencyStop;
    }

    @Override
    public boolean isContainerPaused() {
        return isPaused() && this.listenerConsumer != null;
    }

    @Override
    public boolean isInExpectedState() {
        return isRunning() || isStoppedNormally();
    }

    @Override
    public void pause() {
        super.pause();
        ListenerConsumer consumer = this.listenerConsumer;
        if (consumer != null) {
            consumer.wakeIfNecessary();
        }
    }

    @Override
    public void resume() {
        super.resume();
        ListenerConsumer consumer = this.listenerConsumer;
        if (consumer != null) {
            this.listenerConsumer.wakeIfNecessary();
        }
    }

    @Override
    protected void doStart() {
        if (isRunning()) {
            return;
        }

        ContainerProperties containerProperties = getContainerProperties();
        Object messageListener = containerProperties.getMessageListener();
        AsyncListenableTaskExecutor consumerExecutor = containerProperties.getConsumerTaskExecutor();
        if (consumerExecutor == null) {
            consumerExecutor = new SimpleAsyncTaskExecutor(
                    ( getBeanName() == null ? "" : getBeanName() ) + "-C-" );
            containerProperties.setConsumerTaskExecutor( consumerExecutor );
        }

        this.listenerConsumer = new ListenerConsumer( (GenericMessageListener<?>) messageListener );
        setRunning( true );
        this.startLatch = new CountDownLatch( 1 );
        this.listenerConsumerFuture = consumerExecutor
                .submitListenable( this.listenerConsumer );
        try {
            if (!this.startLatch.await( containerProperties.getConsumerStartTimeout().toMillis(), TimeUnit.MILLISECONDS )) {
                log.error( "Consumer thread failed to start - does the configured task executor "
                        + "have enough threads to support all containers and concurrency?" );
            }
        } catch (@SuppressWarnings(UNUSED) InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void doStop(final Runnable callback, boolean normal) {
        if (isRunning()) {
            this.listenerConsumerFuture.addCallback( new StopCallback( callback ) );
            setRunning( false );
            this.listenerConsumer.wakeIfNecessaryForStop();
            setStoppedNormally( normal );
        }
    }

    @Override
    protected AbstractMessageListenerContainer parentOrThis() {
        return this.thisOrParentContainer;
    }

    @Override
    public String toString() {
        return "DataHubMessageListenerContainer [id=" + getBeanName() + "]";
    }

    private final class ListenerConsumer implements SchedulingAwareRunnable {
        private GenericMessageListener<?> genericListener;

        private Consumer consumer;
        private final ContainerProperties containerProperties = getContainerProperties();
        private int maxRetry = containerProperties.getMaxRetry();
        private volatile Thread consumerThread;

        public ListenerConsumer(GenericMessageListener<?> messageListener) {
            this.genericListener = messageListener;
            this.consumer = createConsumer();

        }

        @Override
        public boolean isLongLived() {
            return true;
        }

        @Override
        public void run() {
            this.consumerThread = Thread.currentThread();
            try {
                while (isRunning()) {
                    try {
                        while (true) {
                            RecordEntry record = this.consumer.read( maxRetry );
                            if (genericListener instanceof MessageListener) {
                                ( (MessageListener) genericListener ).onMessage( record );
                            }
                            if (record != null) {
                                record.getKey().ack();
                            } else {
                                log.trace( "------ record is null,ignored------" );
                            }
                        }
                    } catch (SubscriptionOffsetResetException e) {
                        //点位被重置
                        try {
                            this.consumer.close();
                            this.consumer = createConsumer();
                        } catch (DatahubClientException e1) {
                            log.error( "create consumer failed", e1 );
                            throw e;
                        }
                    } catch (InvalidParameterException | SubscriptionOfflineException
                             | SubscriptionSessionInvalidException | AuthorizationFailureException
                             | NoPermissionException e) {
                        log.error( "read failed", e );
                        throw e;
                    } catch (DatahubClientException e) {
                        log.error( "read failed,entry", e );
                        sleep( 1000 );
                    }
                }
            } catch (Throwable e) {
                log.error( "read failed", e );
            } finally {
                consumer.close();
            }

        }

        public void wakeIfNecessary() {
        }

        public void wakeIfNecessaryForStop() {

        }

        private Consumer createConsumer() {
            DataHubListenerEndpoint listenerEndpoint = containerProperties.getListenerEndpoint();
            ConsumerConfig consumerConfig = new ConsumerConfig( listenerEndpoint.getEndpoint(),
                    listenerEndpoint.getAccessId(), listenerEndpoint.getAccessKey() );
            return new Consumer( listenerEndpoint.getProject(), listenerEndpoint.getTopic(),
                    listenerEndpoint.getSubId(), consumerConfig );
        }
    }

    private class StopCallback implements ListenableFutureCallback<Object> {

        private final Runnable callback;

        StopCallback(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable e) {
            log.error(  "Error while stopping the container: ", e );
            if (this.callback != null) {
                this.callback.run();
            }
        }

        @Override
        public void onSuccess(Object result) {
            log.debug( DataHubMessageListenerContainer.this + " stopped normally" );
            if (this.callback != null) {
                this.callback.run();
            }
        }

    }

}
