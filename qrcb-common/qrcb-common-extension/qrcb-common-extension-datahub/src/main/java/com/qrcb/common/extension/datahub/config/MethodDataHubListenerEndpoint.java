package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;
import com.qrcb.common.extension.datahub.listener.adapter.HandlerAdapter;
import com.qrcb.common.extension.datahub.listener.adapter.MessagingMessageListenerAdapter;
import com.qrcb.common.extension.datahub.listener.adapter.RecordMessagingMessageListenerAdapter;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.core.log.LogAccessor;
import org.springframework.expression.BeanResolver;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class MethodDataHubListenerEndpoint extends AbstractDataHubListenerEndpoint {
    private final LogAccessor logger = new LogAccessor( LogFactory.getLog( getClass() ) );

    private Object bean;

    private Method method;

    /**
     * Set the object instance that should manage this endpoint.
     *
     * @param bean the target bean instance.
     */
    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return this.bean;
    }

    /**
     * Set the method to invoke to process a message managed by this endpoint.
     *
     * @param method the target method for the {@link #bean}.
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }

    @Override
    protected MessagingMessageListenerAdapter createMessageListener(MessageListenerContainer container) {
        MessagingMessageListenerAdapter messageListener = createMessageListenerInstance();
        messageListener.setHandlerMethod( configureListenerAdapter( messageListener ) );
        return messageListener;
    }

    /**
     * Create a {@link HandlerAdapter} for this listener adapter.
     *
     * @param messageListener the listener adapter.
     *
     * @return the handler adapter.
     */
    protected HandlerAdapter configureListenerAdapter(MessagingMessageListenerAdapter messageListener) {
        InvocableHandlerMethod invocableHandlerMethod =
                new InvocableHandlerMethod(getBean(), getMethod());;
        return new HandlerAdapter( invocableHandlerMethod );
    }

    /**
     * Create an empty {@link MessagingMessageListenerAdapter} instance.
     *
     * @return the {@link MessagingMessageListenerAdapter} instance.
     */
    protected MessagingMessageListenerAdapter createMessageListenerInstance() {

        MessagingMessageListenerAdapter listener = new RecordMessagingMessageListenerAdapter(
                this.bean, this.method );
        BeanResolver resolver = getBeanResolver();
        if (resolver != null) {
            listener.setBeanResolver( resolver );
        }
        return listener;
    }

    @SuppressWarnings("null")
    private String resolve(String value) {
        BeanExpressionContext beanExpressionContext = getBeanExpressionContext();
        BeanExpressionResolver resolver = getResolver();
        if (resolver != null && beanExpressionContext != null) {
            Object newValue = resolver.evaluate( value, beanExpressionContext );
            Assert.isInstanceOf( String.class, newValue, "Invalid @SendTo expression" );
            return (String) newValue;
        } else {
            return value;
        }
    }

    @Override
    protected StringBuilder getEndpointDescription() {
        return super.getEndpointDescription()
                .append( " | bean='" ).append( this.bean ).append( "'" )
                .append( " | method='" ).append( this.method ).append( "'" );
    }

}
