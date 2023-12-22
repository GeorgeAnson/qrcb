package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public interface ListenerContainerRegistry {
    @Nullable
    MessageListenerContainer getListenerContainer(String var1);

    Set<String> getListenerContainerIds();

    Collection<MessageListenerContainer> getListenerContainers();

    Collection<MessageListenerContainer> getAllListenerContainers();
}
