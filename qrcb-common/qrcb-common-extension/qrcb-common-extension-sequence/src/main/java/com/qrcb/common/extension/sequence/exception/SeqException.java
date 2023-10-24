package com.qrcb.common.extension.sequence.exception;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号生成异常 <br/>
 */

public class SeqException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SeqException(String message) {
        super(message);
    }

    public SeqException(Throwable cause) {
        super(cause);
    }

}
