package com.qrcb.common.extension.datahub.listener;

import org.springframework.core.task.AsyncListenableTaskExecutor;

import java.time.Duration;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class ContainerProperties extends ConsumerProperties {

    /**
     * The executor for threads that poll the consumer.
     */
    private AsyncListenableTaskExecutor consumerTaskExecutor;
    private Duration consumerStartTimeout = Duration.ofMillis( 10000 );
    private GenericMessageListener<?> messageListener;

    public AsyncListenableTaskExecutor getConsumerTaskExecutor() {
        return consumerTaskExecutor;
    }

    public void setConsumerTaskExecutor(AsyncListenableTaskExecutor consumerTaskExecutor) {
        this.consumerTaskExecutor = consumerTaskExecutor;
    }

    public Duration getConsumerStartTimeout() {
        return consumerStartTimeout;
    }

    public void setConsumerStartTimeout(Duration consumerStartTimeout) {
        this.consumerStartTimeout = consumerStartTimeout;
    }

    public GenericMessageListener<?> getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(GenericMessageListener<?> messageListener) {
        this.messageListener = messageListener;
    }

}
