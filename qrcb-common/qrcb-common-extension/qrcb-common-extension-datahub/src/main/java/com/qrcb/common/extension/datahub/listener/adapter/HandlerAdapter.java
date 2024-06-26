package com.qrcb.common.extension.datahub.listener.adapter;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class HandlerAdapter {
    private final InvocableHandlerMethod invokerHandlerMethod;

    private final DelegatingInvocableHandler delegatingHandler;

    /**
     * Construct an instance with the provided method.
     *
     * @param invokerHandlerMethod the method.
     */
    public HandlerAdapter(InvocableHandlerMethod invokerHandlerMethod) {
        this.invokerHandlerMethod = invokerHandlerMethod;
        this.delegatingHandler = null;
    }

    /**
     * Construct an instance with the provided delegating handler.
     *
     * @param delegatingHandler the handler.
     */
    public HandlerAdapter(DelegatingInvocableHandler delegatingHandler) {
        this.invokerHandlerMethod = null;
        this.delegatingHandler = delegatingHandler;
    }

    public void invoke(Message<?> message, Object... providedArgs) throws Exception { //NOSONAR
        if (this.invokerHandlerMethod != null) {
            this.invokerHandlerMethod.invoke( message, providedArgs ); // NOSONAR
        } else if (this.delegatingHandler.hasDefaultHandler()) {
            // Needed to avoid returning raw Message which matches Object
            Object[] args = new Object[providedArgs.length + 1];
            args[0] = message.getPayload();
            System.arraycopy( providedArgs, 0, args, 1, providedArgs.length );
            this.delegatingHandler.invoke( message, args );
        } else {
            this.delegatingHandler.invoke( message, providedArgs );
        }
    }

    public String getMethodAsString(Object payload) {
        if (this.invokerHandlerMethod != null) {
            return this.invokerHandlerMethod.getMethod().toGenericString();
        } else {
            return this.delegatingHandler.getMethodNameFor( payload );
        }
    }

    public Object getBean() {
        if (this.invokerHandlerMethod != null) {
            return this.invokerHandlerMethod.getBean();
        } else {
            return this.delegatingHandler.getBean();
        }
    }
}
