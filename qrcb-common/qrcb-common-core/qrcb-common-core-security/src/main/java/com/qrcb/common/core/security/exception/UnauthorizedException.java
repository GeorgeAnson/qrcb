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
public class UnauthorizedException extends QrcbAuth2Exception {

    public UnauthorizedException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "unauthorized";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

}
