package com.qrcb.common.extension.datahub.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class ConcurrentMessageListenerContainer extends AbstractMessageListenerContainer {

    @Getter
    private final List<DataHubMessageListenerContainer> containers = new ArrayList<>();

    @Getter
    private final List<AsyncListenableTaskExecutor> executors = new ArrayList<>();

    @Getter
    @Setter
    private int concurrency = 1;

    /**
     * Construct an instance with the provided factory and properties.
     *
     * @param containerProperties
     */
    public ConcurrentMessageListenerContainer(ContainerProperties containerProperties) {
        super( containerProperties );
    }

    @Override
    public boolean isContainerPaused() {
        synchronized (this.lifecycleMonitor) {
            boolean paused = isPaused();
            if (paused) {
                for (AbstractMessageListenerContainer container : this.containers) {
                    if (!container.isContainerPaused()) {
                        return false;
                    }
                }
            }
            return paused;
        }
    }

    @Override
    public boolean isChildRunning() {
        if (!isRunning()) {
            return false;
        }
        for (MessageListenerContainer container : this.containers) {
            if (container.isRunning()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doStart() {
        if (!isRunning()) {
            ContainerProperties containerProperties = getContainerProperties();
            setRunning( true );
            for (int i = 0; i < this.concurrency; i++) {
                DataHubMessageListenerContainer container = constructContainer( containerProperties, i );
                configureChildContainer( i, container );
                if (isPaused()) {
                    container.pause();
                }
                container.start();
                this.containers.add( container );
            }
        }
    }

    private void configureChildContainer(int index, DataHubMessageListenerContainer container) {
        String beanName = getBeanName();
        beanName = ( beanName == null ? "consumer" : beanName ) + "-" + index;
        container.setBeanName( beanName );
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext != null) {
            container.setApplicationContext( applicationContext );
        }
        ApplicationEventPublisher publisher = getApplicationEventPublisher();
        if (publisher != null) {
            container.setApplicationEventPublisher( publisher );
        }
        container.setEmergencyStop( () -> {
            stopAbnormally( () -> {
            } );
        } );
        AsyncListenableTaskExecutor exec = container.getContainerProperties().getConsumerTaskExecutor();
        if (exec == null) {
            if (( this.executors.size() > index )) {
                exec = this.executors.get( index );
            } else {
                exec = new SimpleAsyncTaskExecutor( beanName + "-C-" );
                this.executors.add( exec );
            }
            container.getContainerProperties().setConsumerTaskExecutor( exec );
        }
    }

    private DataHubMessageListenerContainer constructContainer(ContainerProperties containerProperties, int i) {
        return new DataHubMessageListenerContainer( containerProperties );
    }

    @Override
    protected void doStop(final Runnable callback, boolean normal) {
        final AtomicInteger count = new AtomicInteger();
        if (isRunning()) {
            boolean childRunning = isChildRunning();
            setRunning(false);
            if (!childRunning) {
                callback.run();
            }
            for (DataHubMessageListenerContainer container : this.containers) {
                if (container.isRunning()) {
                    count.incrementAndGet();
                }
            }
            for (DataHubMessageListenerContainer container : this.containers) {
                if (container.isRunning()) {
                    if (normal) {
                        container.stop(() -> {
                            if (count.decrementAndGet() <= 0) {
                                callback.run();
                            }
                        });
                    }
                    else {
                        container.stopAbnormally(() -> {
                            if (count.decrementAndGet() <= 0) {
                                callback.run();
                            }
                        });
                    }
                }
            }
            this.containers.clear();
            setStoppedNormally(normal);
        }
    }

    @Override
    public void pause() {
        synchronized (this.lifecycleMonitor) {
            super.pause();
            this.containers.forEach( AbstractMessageListenerContainer ::pause);
        }
    }

    @Override
    public void resume() {
        synchronized (this.lifecycleMonitor) {
            super.resume();
            this.containers.forEach( AbstractMessageListenerContainer ::resume);
        }
    }

}
