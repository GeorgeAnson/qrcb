package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.AbstractMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public abstract class AbstractDataHubListenerContainerFactory<C extends AbstractMessageListenerContainer> implements DataHubListenerContainerFactory<C>,
        ApplicationEventPublisherAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ApplicationEventPublisher applicationEventPublisher;
    private Boolean autoStartup = true;

    @Override
    public C createListenerContainer(DataHubListenerEndpoint endpoint) {
        C instance = createListenerInstance( endpoint );
        initializeContainer( instance, endpoint );
        return instance;
    }

    protected abstract C createListenerInstance(DataHubListenerEndpoint endpoint);

    protected void initializeContainer(C instance, DataHubListenerEndpoint endpoint) {
        instance.setApplicationContext( applicationContext );
        instance.setApplicationEventPublisher( applicationEventPublisher );
        Boolean autoStart = endpoint.getAutoStartup();
        if (autoStart != null) {
            instance.setAutoStartup( autoStart );
        } else if (this.autoStartup != null) {
            instance.setAutoStartup( this.autoStartup );
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
