package com.qrcb.common.extension.datahub.listener.adapter;

import com.aliyun.datahub.client.model.RecordEntry;
import com.qrcb.common.extension.datahub.exception.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.lang.reflect.Method;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class RecordMessagingMessageListenerAdapter extends MessagingMessageListenerAdapter {

    public RecordMessagingMessageListenerAdapter(Object bean, Method method) {
        super( bean, method );
    }

    /**
     * <p> Delegate the message to the target listener method,
     * with appropriate conversion of the message argument.
     *
     * @param record the incoming DataHubu {@link RecordEntry}.
     */
    @Override
    public void onMessage(RecordEntry record) {
        Message<?> message;
        if (record != null) {
            message = new GenericMessage<>( record );
        } else {
            message = NULL_MESSAGE;
        }
        if (logger.isDebugEnabled()) {
            this.logger.debug( "Processing [" + message + "]" );
        }
        try {
            invokeHandler( message,record );
        } catch (ListenerExecutionFailedException e) { // NOSONAR ex flow control
            if (message.equals( NULL_MESSAGE )) {
                return;
//                throw new ListenerExecutionFailedException( createMessagingErrorMessage(// NOSONAR stack trace loss
//                        "Listener error handler threw an exception for the incoming message",
//                        message.getPayload() ), e );
            }
            throw e;

        }
    }
}
