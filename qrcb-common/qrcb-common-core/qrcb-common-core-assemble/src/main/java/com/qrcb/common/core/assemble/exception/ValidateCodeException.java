package com.qrcb.common.core.assemble.exception;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 验证码异常 <br/>
 */

public class ValidateCodeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
