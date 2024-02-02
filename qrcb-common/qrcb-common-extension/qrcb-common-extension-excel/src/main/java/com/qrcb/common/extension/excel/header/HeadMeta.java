package com.qrcb.common.extension.excel.header;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel 表头元信息 <br/>
 */

@Data
public class HeadMeta {

    /**
     * <p>
     * 自定义头部信息
     * </p>
     * 实现类根据数据的class信息，定制Excel头<br/>
     * 具体方法使用参考：<a href="https://www.yuque.com/easyexcel/doc/write#b4b9de00">写Excel</a>
     */
    private List<List<String>> head;

    /**
     * 忽略头对应字段名称
     */
    private Set<String> ignoreHeadFields;
}
