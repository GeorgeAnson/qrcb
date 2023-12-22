package com.qrcb.common.extension.datahub.support;

import lombok.experimental.UtilityClass;
import org.springframework.messaging.Message;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@UtilityClass
public class DataHubUtils {

    /**
     * Return true if the method return type is {@link Message} or
     * {@code Collection<Message<?>>}.
     * @param method the method.
     * @return true if it returns message(s).
     */
    public boolean returnTypeMessageOrCollectionOf(Method method) {
        Type returnType = method.getGenericReturnType();
        if (returnType.equals(Message.class)) {
            return true;
        }
        if (returnType instanceof ParameterizedType) {
            ParameterizedType prt = (ParameterizedType) returnType;
            Type rawType = prt.getRawType();
            if (rawType.equals(Message.class)) {
                return true;
            }
            if (rawType.equals( Collection.class)) {
                Type collectionType = prt.getActualTypeArguments()[0];
                if (collectionType.equals(Message.class)) {
                    return true;
                }
                return collectionType instanceof ParameterizedType
                        && ((ParameterizedType) collectionType).getRawType().equals(Message.class);
            }
        }
        return false;

    }
}
