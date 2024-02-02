package com.qrcb.common.extension.excel.handler;

import com.alibaba.excel.event.AnalysisEventListener;
import com.qrcb.common.extension.excel.kit.ErrorMessage;

import java.util.List;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel 数据解析监听器 <br/>
 */

public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {


    /**
     * 获取 excel 解析的对象列表
     *
     * @return 集合
     */
    public abstract List<T> getList();

    /**
     * 获取异常校验结果
     *
     * @return 集合
     */
    public abstract List<ErrorMessage> getErrors();
}
