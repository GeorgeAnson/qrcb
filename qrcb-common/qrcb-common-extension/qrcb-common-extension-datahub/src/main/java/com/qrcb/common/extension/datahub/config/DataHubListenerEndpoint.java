package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public interface DataHubListenerEndpoint {
    @Nullable
    String getId();

    String getProject();

    String getSubId();

    String getTopic();

    String getAccessId();

    String getAccessKey();

    String getEndpoint();

    @Nullable
    Integer getConcurrency();

    @Nullable
    Boolean getAutoStartup();

    @Nullable
    default Properties getConfigProperties() {
        return null;
    }

    void setupListenerContainer(MessageListenerContainer listenerContainer);

    String getGroup();

}
