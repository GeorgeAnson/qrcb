package com.qrcb.common.extension.excel.header;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel头生成器，用于自定义生成头部信息 <br/>
 */

public interface HeadGenerator {

    /**
     * <p>
     * 自定义头部信息
     * </p>
     * 实现类根据数据的class信息，定制Excel头<br/>
     * 具体方法使用参考：<a href="https://www.yuque.com/easyexcel/doc/write#b4b9de00">写Excel</a>
     *
     * @param oneRowData 当前sheet的首行数据
     * @return {@link HeadMeta} Header头信息
     */
    HeadMeta head(Object oneRowData);
}
