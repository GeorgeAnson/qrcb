package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public interface DataHubListenerContainerFactory<C extends MessageListenerContainer> {
    C createListenerContainer(DataHubListenerEndpoint endpoint);
}
