package com.qrcb.common.extension.datahub.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class DataHubListenerEndpointRegistrar implements BeanFactoryAware, InitializingBean {
    private final List<DataHubListenerEndpointDescriptor> endpointDescriptors = new ArrayList<>();

    private DataHubListenerEndpointRegistry endpointRegistry;
    private String containerFactoryBeanName;
    private BeanFactory beanFactory;
    private boolean startImmediately;
    private DataHubListenerContainerFactory<?> containerFactory;


    public void setEndpointRegistry(DataHubListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    public void setContainerFactoryBeanName(String containerFactoryBeanName) {
        this.containerFactoryBeanName = containerFactoryBeanName;
    }

    /**
     * Return the {@link DataHubListenerEndpointRegistry} instance for this
     * registrar, may be {@code null}.
     *
     * @return the {@link DataHubListenerEndpointRegistry} instance for this
     * registrar, may be {@code null}.
     */
    @Nullable
    public DataHubListenerEndpointRegistry getEndpointRegistry() {
        return this.endpointRegistry;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        registerAllEndpoints();
    }

    protected void registerAllEndpoints() {
        synchronized (this.endpointDescriptors) {
            for (DataHubListenerEndpointDescriptor descriptor : this.endpointDescriptors) {
                this.endpointRegistry.registerListenerContainer(
                        descriptor.endpoint, resolveContainerFactory( descriptor ) );
            }
            this.startImmediately = true;  // trigger immediate startup
        }
    }

    public void registerEndpoint(MethodDataHubListenerEndpoint endpoint, DataHubListenerContainerFactory<?> factory) {
        Assert.notNull( endpoint, "Endpoint must be set" );
        Assert.hasText( endpoint.getId(), "Endpoint id must be set" );
        // Factory may be null, we defer the resolution right before actually creating the container
        DataHubListenerEndpointDescriptor descriptor = new DataHubListenerEndpointDescriptor( endpoint, factory );
        synchronized (this.endpointDescriptors) {
            if (this.startImmediately) { // Register and start immediately
                this.endpointRegistry.registerListenerContainer( descriptor.endpoint,
                        resolveContainerFactory( descriptor ), true );
            } else {
                this.endpointDescriptors.add( descriptor );
            }
        }
    }

    private DataHubListenerContainerFactory<?> resolveContainerFactory(DataHubListenerEndpointDescriptor descriptor) {
        if (descriptor.containerFactory != null) {
            return descriptor.containerFactory;
        } else if (this.containerFactory != null) {
            return this.containerFactory;
        } else if (this.containerFactoryBeanName != null) {
            Assert.state( this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name" );
            this.containerFactory = this.beanFactory.getBean(
                    this.containerFactoryBeanName, DataHubListenerContainerFactory.class );
            return this.containerFactory;  // Consider changing this if live change of the factory is required
        } else {
            throw new IllegalStateException( "Could not resolve the " +
                    DataHubListenerContainerFactory.class.getSimpleName() + " to use for [" +
                    descriptor.endpoint + "] no factory was given and no default is set." );
        }
    }

    private static final class DataHubListenerEndpointDescriptor {

        private final DataHubListenerEndpoint endpoint;

        private final DataHubListenerContainerFactory<?> containerFactory;

        private DataHubListenerEndpointDescriptor(DataHubListenerEndpoint endpoint,
                                                  @Nullable DataHubListenerContainerFactory<?> containerFactory) {

            this.endpoint = endpoint;
            this.containerFactory = containerFactory;
        }

    }
}
