package com.qrcb.common.extension.excel.kit;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel 统一异常 <br/>
 */

public class ExcelException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ExcelException(String message) {
        super(message);
    }

}
