package com.qrcb.common.extension.datahub.listener;

import com.qrcb.common.extension.datahub.event.ContainerStoppedEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */


public abstract class AbstractMessageListenerContainer implements MessageListenerContainer, BeanNameAware, ApplicationEventPublisherAware,
        ApplicationContextAware {

    /**
     * The default {@link org.springframework.context.SmartLifecycle} phase for listener
     * containers {@value #DEFAULT_PHASE}.
     */
    public static final int DEFAULT_PHASE = Integer.MAX_VALUE - 100; // late phase

    protected final Object lifecycleMonitor = new Object(); // NOSONAR

    private String beanName;

    private ApplicationEventPublisher applicationEventPublisher;


    private boolean autoStartup = true;

    private int phase = DEFAULT_PHASE;

    private volatile boolean running = false;

    private volatile boolean paused;

    private ContainerProperties containerProperties;

    private ApplicationContext applicationContext;

    private volatile boolean stoppedNormally = true;

    /**
     * Construct an instance with the provided factory and properties.
     *
     */
    @SuppressWarnings("unchecked")
    protected AbstractMessageListenerContainer(
            ContainerProperties containerProperties) {

        Assert.notNull( containerProperties, "'consumerProperties' cannot be null" );
        this.containerProperties = containerProperties;
        this.containerProperties.getListenerEndpoint().setupListenerContainer( this );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Nullable
    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * Return the bean name.
     *
     * @return the bean name.
     */
    @Nullable
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Get the event publisher.
     *
     * @return the publisher
     */
    @Nullable
    public ApplicationEventPublisher getApplicationEventPublisher() {
        return this.applicationEventPublisher;
    }

    @Override
    public void setupMessageListener(MessageListener messageListener) {
        this.containerProperties.setMessageListener(messageListener);
    }
    protected boolean isStoppedNormally() {
        return this.stoppedNormally;
    }

    protected void setStoppedNormally(boolean stoppedNormally) {
        this.stoppedNormally = stoppedNormally;
    }

    @Override
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    @Override
    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    protected boolean isPaused() {
        return this.paused;
    }

    @Override
    public boolean isPauseRequested() {
        return this.paused;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public int getPhase() {
        return this.phase;
    }

    public ContainerProperties getContainerProperties() {
        return this.containerProperties;
    }

    @Override
    @Nullable
    public String getListenerId() {
        return this.beanName; // the container factory sets the bean name to the id attribute
    }

    @Override
    public final void start() {
        //checkGroupId();
        synchronized (this.lifecycleMonitor) {
            if (!isRunning()) {
                Assert.state( this.containerProperties.getMessageListener() instanceof MessageListener,
                        () -> "A " + GenericMessageListener.class.getName() + " implementation must be provided" );
                doStart();
            }
        }
    }

    protected abstract void doStart();

    @Override
    public final void stop() {
        stop( true );
    }

    /**
     * Stop the container.
     *
     * @param wait wait for the listener to terminate.
     *
     * @since 2.3.8
     */
    public final void stop(boolean wait) {
        synchronized (this.lifecycleMonitor) {
            if (isRunning()) {
                if (wait) {
                    final CountDownLatch latch = new CountDownLatch( 1 );
                    doStop( latch :: countDown );
                    try {
                        latch.await( this.containerProperties.getShutdownTimeout(), TimeUnit.MILLISECONDS ); // NOSONAR
                        publishContainerStoppedEvent();
                    } catch (@SuppressWarnings("unused") InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    doStop( this :: publishContainerStoppedEvent );
                }
            }
        }
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public void stop(Runnable callback) {
        synchronized (this.lifecycleMonitor) {
            if (isRunning()) {
                doStop( callback );
            } else {
                callback.run();
            }
        }
    }

    @Override
    public void stopAbnormally(Runnable callback) {
        doStop( callback, false );
        publishContainerStoppedEvent();
    }

    protected void doStop(Runnable callback) {
        doStop( callback, true );
        publishContainerStoppedEvent();
    }

    /**
     * Stop the container normally or abnormally.
     *
     * @param callback the callback.
     * @param normal true for an expected stop.
     *
     * @since 2.8
     */
    protected abstract void doStop(Runnable callback, boolean normal);

    protected void publishContainerStoppedEvent() {
        ApplicationEventPublisher eventPublisher = getApplicationEventPublisher();
        if (eventPublisher != null) {
            eventPublisher.publishEvent( new ContainerStoppedEvent( this, parentOrThis() ) );
        }
    }

    /**
     * Return this or a parent container if this has a parent.
     *
     * @return the parent or this.
     *
     * @since 2.2.1
     */
    protected AbstractMessageListenerContainer parentOrThis() {
        return this;
    }
}
