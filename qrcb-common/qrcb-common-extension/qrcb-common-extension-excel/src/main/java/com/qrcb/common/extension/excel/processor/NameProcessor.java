package com.qrcb.common.extension.excel.processor;

import java.lang.reflect.Method;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel 导出，文件名称解析器接口 <br/>
 */

public interface NameProcessor {

    /**
     * 解析名称
     *
     * @param args   拦截器对象
     * @param method 拦截的函数方法
     * @param key    表达式
     * @return 名称
     */
    String doDetermineName(Object[] args, Method method, String key);
}
