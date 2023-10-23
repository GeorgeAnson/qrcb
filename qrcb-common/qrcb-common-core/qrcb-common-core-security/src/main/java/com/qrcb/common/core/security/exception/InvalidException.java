package com.qrcb.common.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qrcb.common.core.security.component.QrcbAuth2ExceptionSerializer;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description <br/>
 */

@JsonSerialize(using = QrcbAuth2ExceptionSerializer.class)
public class InvalidException extends QrcbAuth2Exception {

    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    @Override
    public int getHttpErrorCode() {
        return 426;
    }

}