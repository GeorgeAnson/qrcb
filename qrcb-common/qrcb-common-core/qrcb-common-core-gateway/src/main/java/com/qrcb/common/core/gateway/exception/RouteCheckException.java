package com.qrcb.common.core.gateway.exception;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description <br/>
 */

public class RouteCheckException extends RuntimeException {

    public RouteCheckException() {
    }

    public RouteCheckException(String message) {
        super(message);
    }

    public RouteCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteCheckException(Throwable cause) {
        super(cause);
    }

    public RouteCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
