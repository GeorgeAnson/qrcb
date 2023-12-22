package com.qrcb.common.extension.datahub.listener;

import com.aliyun.datahub.client.model.RecordEntry;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@FunctionalInterface
public interface MessageListener extends GenericMessageListener<RecordEntry> {
    void onMessage(RecordEntry recordEntry);
}
