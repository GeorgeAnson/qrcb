package com.qrcb.common.extension.excel.handler;

import com.qrcb.common.extension.excel.annotation.ResponseExcel;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description Sheet 写出处理器 <br/>
 */

public interface SheetWriteHandler {


    /**
     * 是否支持
     *
     * @param obj 写入的数据
     * @return boolean
     */
    boolean support(Object obj);

    /**
     * 校验
     *
     * @param responseExcel 注解
     */
    void check(ResponseExcel responseExcel);

    /**
     * 返回的对象
     *
     * @param obj           object
     * @param response      输出对象
     * @param responseExcel 注解
     */
    void export(Object obj, HttpServletResponse response, ResponseExcel responseExcel);

    /**
     * 写成对象
     *
     * @param obj           object
     * @param response      输出对象
     * @param responseExcel 注解
     */
    void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel);

}
