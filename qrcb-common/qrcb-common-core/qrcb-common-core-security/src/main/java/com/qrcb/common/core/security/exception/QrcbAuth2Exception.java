package com.qrcb.common.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qrcb.common.core.security.component.QrcbAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 自定义OAuth2Exception <br/>
 */

@JsonSerialize(using = QrcbAuth2ExceptionSerializer.class)
public class QrcbAuth2Exception extends OAuth2Exception {


    @Getter
    private String errorCode;

    public QrcbAuth2Exception(String msg) {
        super(msg);
    }

    public QrcbAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public QrcbAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }


}
