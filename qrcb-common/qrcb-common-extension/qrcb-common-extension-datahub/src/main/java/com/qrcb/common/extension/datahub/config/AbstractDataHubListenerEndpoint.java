package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.MessageListener;
import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;
import com.qrcb.common.extension.datahub.listener.adapter.MessagingMessageListenerAdapter;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.log.LogAccessor;
import org.springframework.expression.BeanResolver;
import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public abstract class AbstractDataHubListenerEndpoint
        implements DataHubListenerEndpoint, BeanFactoryAware, InitializingBean {
    private final LogAccessor logger = new LogAccessor( LogFactory.getLog( getClass() ) );

    private String id;

    private String group;

    private String topic;

    private String project;

    private BeanFactory beanFactory;

    private BeanExpressionResolver resolver;

    private BeanExpressionContext expressionContext;

    private BeanResolver beanResolver;

    private Integer concurrency;

    private Boolean autoStartup;

    private Properties configProperties;

    private String subId;

    private String endpoint;

    private String accessId;

    private String accessKey;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ( (ConfigurableListableBeanFactory) beanFactory ).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext( (ConfigurableListableBeanFactory) beanFactory, null );
        }
        this.beanResolver = new BeanFactoryResolver( beanFactory );
    }

    @Nullable
    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Nullable
    protected BeanExpressionResolver getResolver() {
        return this.resolver;
    }

    @Nullable
    protected BeanExpressionContext getBeanExpressionContext() {
        return this.expressionContext;
    }

    @Nullable
    protected BeanResolver getBeanResolver() {
        return this.beanResolver;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSubId() {
        return this.subId;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public String getAccessId() {
        return this.accessId;
    }

    @Override
    public String getAccessKey() {
        return this.accessKey;
    }

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Nullable
    @Override
    public String getProject() {
        return this.project;
    }

    @Nullable
    @Override
    public String getGroup() {
        return this.group;
    }

    /**
     * Set the group for the corresponding listener container.
     *
     * @param group the group.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    @Nullable
    public Integer getConcurrency() {
        return this.concurrency;
    }

    /**
     * Set the concurrency for this endpoint's container.
     *
     * @param concurrency the concurrency.
     *
     * @since 2.2
     */
    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    @Override
    @Nullable
    public Boolean getAutoStartup() {
        return this.autoStartup;
    }

    /**
     * Set the autoStartup for this endpoint's container.
     *
     * @param autoStartup the autoStartup.
     *
     * @since 2.2
     */
    public void setAutoStartup(Boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    @Nullable
    public Properties getConfigProperties() {
        return this.configProperties;
    }

    /**
     * Set the consumer properties that will be merged with the consumer properties
     * provided by the consumer factory; properties here will supersede any with the same
     * name(s) in the consumer factory.
     * {@code group.id} and {@code client.id} are ignored.
     *
     * @param configProperties the properties.
     *
     * @since 2.1.4
     */
    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public void afterPropertiesSet() {
        //nothing to do
    }

    @Override
    public void setupListenerContainer(MessageListenerContainer listenerContainer) {
        setupMessageListener( listenerContainer );
    }

    /**
     * Create a {@link MessageListener} that is able to serve this endpoint for the
     * specified container.
     *
     * @param container the {@link MessageListenerContainer} to create a {@link MessageListener}.
     *
     * @return a {@link MessageListener} instance.
     */
    protected abstract MessagingMessageListenerAdapter createMessageListener(MessageListenerContainer container);

    private void setupMessageListener(MessageListenerContainer container) {
        MessagingMessageListenerAdapter adapter = createMessageListener( container );
        container.setupMessageListener( adapter );
    }

    /**
     * Return a description for this endpoint.
     *
     * @return a description for this endpoint.
     * <p>Available to subclasses, for inclusion in their {@code toString()} result.
     */
    protected StringBuilder getEndpointDescription() {
        StringBuilder result = new StringBuilder();
        return result.append( getClass().getSimpleName() ).append( "[" ).append( this.id ).
                append( "] topic=" ).append( this.topic ).
                append( "' | project='" ).append( this.project ).
                append( "' | subId='" ).append( this.subId ).append( "'" );
    }

    @Override
    public String toString() {
        return getEndpointDescription().toString();
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
