package com.qrcb.common.extension.datahub.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description DataHubException <br/>
 */

public class DataHubException extends NestedRuntimeException {
    public DataHubException(String msg) {
        super( msg );
    }

    public DataHubException(String msg, Throwable cause) {
        super( msg, cause );
    }
}