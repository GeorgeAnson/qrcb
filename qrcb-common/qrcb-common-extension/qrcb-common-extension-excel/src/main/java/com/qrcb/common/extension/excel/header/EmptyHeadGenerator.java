package com.qrcb.common.extension.excel.header;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description 空的 excel 头生成器，用来忽略 excel 头生成 <br/>
 */

public class EmptyHeadGenerator implements HeadGenerator {

    @Override
    public HeadMeta head(Object oneRowData) {
        return new HeadMeta();
    }
}
