package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.ConcurrentMessageListenerContainer;
import com.qrcb.common.extension.datahub.listener.ContainerProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class ConcurrentMessageListenerContainerFactory extends AbstractDataHubListenerContainerFactory<ConcurrentMessageListenerContainer> {

    @Getter
    @Setter
    private Integer concurrency = 1;

    @Override
    protected ConcurrentMessageListenerContainer createListenerInstance(DataHubListenerEndpoint endpoint) {
        ContainerProperties containerProperties = new ContainerProperties();
        containerProperties.setListenerEndpoint( endpoint );
        return new ConcurrentMessageListenerContainer( containerProperties );
    }

    @Override
    protected void initializeContainer(ConcurrentMessageListenerContainer instance,
                                       DataHubListenerEndpoint endpoint) {
        super.initializeContainer( instance, endpoint );
        Integer conc = endpoint.getConcurrency();
        if (conc != null) {
            instance.setConcurrency( conc );
        } else if (this.concurrency != null) {
            instance.setConcurrency( this.concurrency );
        }
    }
}
