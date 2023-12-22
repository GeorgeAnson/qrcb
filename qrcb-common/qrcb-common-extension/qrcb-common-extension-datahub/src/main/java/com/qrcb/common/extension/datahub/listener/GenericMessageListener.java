package com.qrcb.common.extension.datahub.listener;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@FunctionalInterface
public interface GenericMessageListener<T> {

    /**
     * Invoked with data from datahub.
     *
     * @param data the data to be processed.
     */
    void onMessage(T data);

}
