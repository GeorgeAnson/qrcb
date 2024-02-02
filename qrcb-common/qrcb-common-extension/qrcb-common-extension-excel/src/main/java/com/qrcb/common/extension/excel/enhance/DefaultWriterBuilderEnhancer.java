package com.qrcb.common.extension.excel.enhance;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.qrcb.common.extension.excel.annotation.ResponseExcel;
import com.qrcb.common.extension.excel.header.HeadGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description 默认的增强实现， 不做任何增强 <br/>
 */

public class DefaultWriterBuilderEnhancer implements WriterBuilderEnhancer{

    @Override
    public ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
                                           ResponseExcel responseExcel, String templatePath) {
        return writerBuilder;
    }

    @Override
    public ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo,
                                                String sheetName, Object oneRowData, String template, Class<? extends HeadGenerator> headEnhancerClass) {
        return writerSheetBuilder;
    }
}
