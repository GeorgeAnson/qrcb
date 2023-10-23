package com.qrcb.common.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qrcb.common.core.security.component.QrcbAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description <br/>
 */

@JsonSerialize(using = QrcbAuth2ExceptionSerializer.class)
public class MethodNotAllowedException extends QrcbAuth2Exception {

    public MethodNotAllowedException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "method_not_allowed";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.METHOD_NOT_ALLOWED.value();
    }

}
