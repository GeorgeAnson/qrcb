package com.qrcb.common.extension.datahub.listener.adapter;

import com.aliyun.datahub.client.model.RecordEntry;
import com.qrcb.common.extension.datahub.exception.ListenerExecutionFailedException;
import com.qrcb.common.extension.datahub.listener.ConsumerSeekAware;
import com.qrcb.common.extension.datahub.listener.MessageListener;
import com.qrcb.common.extension.datahub.support.DataHubNull;
import lombok.Data;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.MethodParameter;
import org.springframework.core.log.LogAccessor;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public abstract class MessagingMessageListenerAdapter implements ConsumerSeekAware, MessageListener {
    /**
     * Message used when no conversion is needed.
     */
    protected static final Message<DataHubNull> NULL_MESSAGE = new GenericMessage<>( DataHubNull.INSTANCE ); // NOSONAR

    private final Object bean;

    protected final LogAccessor logger = new LogAccessor( LogFactory.getLog( getClass() ) ); //NOSONAR

    private final Type inferredType;

    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    private HandlerAdapter handlerMethod;

    private boolean isConsumerRecordList;

    private boolean isConsumerRecords;

    private boolean isMessageList;

    private Type fallbackType = Object.class;

    private boolean hasAckParameter;
    private boolean conversionNeeded = true;

    public MessagingMessageListenerAdapter(Object bean, Method method) {
        this.bean = bean;
        this.inferredType = determineInferredType( method ); // NOSONAR = intentionally not final
    }

    public boolean isConversionNeeded() {
        return this.conversionNeeded;
    }

    /**
     * Returns the inferred type for conversion or, if null, the
     * {@link #setFallbackType(Class) fallbackType}.
     *
     * @return the type.
     */
    protected Type getType() {
        return this.inferredType == null ? this.fallbackType : this.inferredType;
    }

    /**
     * Set a fallback type to use when using a type-aware message converter and this
     * adapter cannot determine the inferred type from the method. An example of a
     * type-aware message converter is the {@code StringJsonMessageConverter}. Defaults to
     * {@link Object}.
     *
     * @param fallbackType the type.
     */
    public void setFallbackType(Class<?> fallbackType) {
        this.fallbackType = fallbackType;
    }

    /**
     * Set the {@link HandlerAdapter} to use to invoke the method
     * processing an incoming {@link RecordEntry}.
     *
     * @param handlerMethod {@link HandlerAdapter} instance.
     */
    public void setHandlerMethod(HandlerAdapter handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    protected boolean isConsumerRecordList() {
        return this.isConsumerRecordList;
    }

    public boolean isConsumerRecords() {
        return this.isConsumerRecords;
    }

    /**
     * Set a bean resolver for runtime SpEL expressions. Also configures the evaluation
     * context with a standard type converter and map accessor.
     *
     * @param beanResolver the resolver.
     *
     * @since 2.0
     */
    public void setBeanResolver(BeanResolver beanResolver) {
        this.evaluationContext.setBeanResolver( beanResolver );
        this.evaluationContext.setTypeConverter( new StandardTypeConverter() );
        this.evaluationContext.addPropertyAccessor( new MapAccessor() );
    }

    protected boolean isMessageList() {
        return this.isMessageList;
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        if (this.bean instanceof ConsumerSeekAware) {
            ( (ConsumerSeekAware) this.bean ).registerSeekCallback( callback );
        }
    }

    /**
     * Invoke the handler, wrapping any exception to a {@link ListenerExecutionFailedException}
     * with a dedicated error message.
     *
     * @param message the message to process.
     *
     * @return the result of invocation.
     */
    protected final void invokeHandler(Message<?> message,RecordEntry recordEntry) {

        try {
            this.handlerMethod.invoke( message,recordEntry );
        } catch (MessageConversionException ex) {
            throw checkAckArg( message, new MessageConversionException( "Cannot handle message", ex ) );
        } catch (MethodArgumentNotValidException ex) {
            throw checkAckArg( message, ex );
        } catch (MessagingException ex) {
            throw new ListenerExecutionFailedException( createMessagingErrorMessage( "Listener method could not " +
                    "be invoked with the incoming message", message.getPayload() ), ex );
        } catch (Exception ex) {
            throw new ListenerExecutionFailedException( "Listener method '" +
                    this.handlerMethod.getMethodAsString( message.getPayload() ) + "' threw exception", ex );
        }
    }

    private RuntimeException checkAckArg(Message<?> message, Exception ex) {
        if (this.hasAckParameter) {
            return new ListenerExecutionFailedException( "invokeHandler Failed",
                    new IllegalStateException( "No Acknowledgment available as an argument, "
                            + "the listener container must have a MANUAL AckMode to populate the Acknowledgment.",
                            ex ) );
        }
        return new ListenerExecutionFailedException( createMessagingErrorMessage( "Listener method could not " +
                "be invoked with the incoming message", message.getPayload() ), ex );
    }

    protected final String createMessagingErrorMessage(String description, Object payload) {
        return description + "\n"
                + "Endpoint handler details:\n"
                + "Method [" + this.handlerMethod.getMethodAsString( payload ) + "]\n"
                + "Bean [" + this.handlerMethod.getBean() + "]";
    }

    /**
     * Subclasses can override this method to use a different mechanism to determine
     * the target type of the payload conversion.
     *
     * @param method the method.
     *
     * @return the type.
     */
    protected Type determineInferredType(Method method) { // NOSONAR complexity
        if (method == null) {
            return null;
        }

        Type genericParameterType = null;
        int allowedBatchParameters = 1;
        int notConvertibleParameters = 0;

        for (int i = 0; i < method.getParameterCount(); i++) {
            MethodParameter methodParameter = new MethodParameter( method, i );
            /*
             * We're looking for a single non-annotated parameter, or one annotated with @Payload.
             * We ignore parameters with type Message, Consumer, Ack, ConsumerRecord because they
             * are not involved with conversion.
             */
            Type parameterType = methodParameter.getGenericParameterType();
            boolean isNotConvertible = parameterIsType( parameterType, RecordEntry.class );
            boolean isConsumer = parameterIsType( parameterType, RecordEntry.class );
            isNotConvertible |= isConsumer;
            if (isNotConvertible) {
                notConvertibleParameters++;
            }
            if (!isNotConvertible && !isMessageWithNoTypeInfo( parameterType )
                    && ( methodParameter.getParameterAnnotations().length == 0
                    || methodParameter.hasParameterAnnotation( Payload.class ) )) {
                if (genericParameterType == null) {
                    genericParameterType = extractGenericParameterTypFromMethodParameter( methodParameter );
                } else {
                    this.logger.debug( () -> "Ambiguous parameters for target payload for method " + method
                            + "; no inferred type available" );
                    break;
                }
            } else {
                if (isConsumer) {
                    allowedBatchParameters++;
                } else {
                    if (parameterType instanceof ParameterizedType
                            && ( (ParameterizedType) parameterType ).getRawType().equals( RecordEntry.class )) {
                        allowedBatchParameters++;
                    }
                }
            }
        }
        if (notConvertibleParameters == method.getParameterCount() && method.getReturnType().equals( void.class )) {
            this.conversionNeeded = false;
        }
        boolean validParametersForBatch = method.getGenericParameterTypes().length <= allowedBatchParameters;

        if (!validParametersForBatch) {
            String stateMessage = "A parameter of type '%s' must be the only parameter ";
            Assert.state( !this.isConsumerRecords,
                    () -> String.format( stateMessage, "RecordEntry" ) );
            Assert.state( !this.isConsumerRecordList,
                    () -> String.format( stateMessage, "List<RecordEntry>" ) );
            Assert.state( !this.isMessageList,
                    () -> String.format( stateMessage, "List<Message<?>>" ) );
        }
        return genericParameterType;
    }

    private Type extractGenericParameterTypFromMethodParameter(MethodParameter methodParameter) {
        Type genericParameterType = methodParameter.getGenericParameterType();
        if (genericParameterType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
            if (parameterizedType.getRawType().equals( Message.class )) {
                genericParameterType = ( (ParameterizedType) genericParameterType ).getActualTypeArguments()[0];
            } else if (parameterizedType.getRawType().equals( List.class )
                    && parameterizedType.getActualTypeArguments().length == 1) {

                Type paramType = parameterizedType.getActualTypeArguments()[0];
                this.isConsumerRecordList = paramType.equals( RecordEntry.class )
                        || ( isSimpleListOfConsumerRecord( paramType )
                        || isListOfConsumerRecordUpperBounded( paramType ) );
                boolean messageHasGeneric = paramType instanceof ParameterizedType
                        && ( (ParameterizedType) paramType ).getRawType().equals( Message.class );
                this.isMessageList = paramType.equals( Message.class ) || messageHasGeneric;
                if (messageHasGeneric) {
                    genericParameterType = ( (ParameterizedType) paramType ).getActualTypeArguments()[0];
                }
            } else {
                this.isConsumerRecords = parameterizedType.getRawType().equals( RecordEntry.class );
            }
        }
        return genericParameterType;
    }

    private boolean isSimpleListOfConsumerRecord(Type paramType) {
        return paramType instanceof ParameterizedType
                && ( (ParameterizedType) paramType ).getRawType().equals( RecordEntry.class );
    }

    private boolean isListOfConsumerRecordUpperBounded(Type paramType) {
        return isWildCardWithUpperBound( paramType )
                && ( (WildcardType) paramType ).getUpperBounds()[0] instanceof ParameterizedType
                && ( (ParameterizedType) ( (WildcardType) paramType ).getUpperBounds()[0] )
                .getRawType().equals( RecordEntry.class );
    }

    private boolean isWildCardWithUpperBound(Type paramType) {
        return paramType instanceof WildcardType
                && ( (WildcardType) paramType ).getUpperBounds() != null
                && ( (WildcardType) paramType ).getUpperBounds().length > 0;
    }

    private boolean isMessageWithNoTypeInfo(Type parameterType) {
        if (parameterType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) parameterType;
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals( Message.class )) {
                return parameterizedType.getActualTypeArguments()[0] instanceof WildcardType;
            }
        }
        return parameterType.equals( Message.class ); // could be Message without a generic type
    }

    private boolean parameterIsType(Type parameterType, Type type) {
        if (parameterType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) parameterType;
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals( type )) {
                return true;
            }
        }
        return parameterType.equals( type );
    }

    /**
     * Root object for reply expression evaluation.
     *
     * @since 2.0
     */
    @Data
    public static final class ReplyExpressionRoot {

        private final Object request;

        private final Object source;

        private final Object result;

    }
}
