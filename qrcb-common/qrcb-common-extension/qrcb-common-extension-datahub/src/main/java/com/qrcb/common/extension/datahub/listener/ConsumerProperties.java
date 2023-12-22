package com.qrcb.common.extension.datahub.listener;

import com.qrcb.common.extension.datahub.config.DataHubListenerEndpoint;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class ConsumerProperties {

    private DataHubListenerEndpoint listenerEndpoint;

    private long shutdownTimeout = 5000;

    private int maxRetry = 3;

    public DataHubListenerEndpoint getListenerEndpoint() {
        return listenerEndpoint;
    }

    public void setListenerEndpoint(DataHubListenerEndpoint listenerEndpoint) {
        this.listenerEndpoint = listenerEndpoint;
    }

    public long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }


    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }
}
