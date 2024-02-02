package com.qrcb.common.extension.excel.enhance;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.qrcb.common.extension.excel.annotation.ResponseExcel;
import com.qrcb.common.extension.excel.header.HeadGenerator;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description ExcelWriterBuilder 增强 <br/>
 */

public interface WriterBuilderEnhancer {

    /**
     * ExcelWriterBuilder 增强
     *
     * @param writerBuilder ExcelWriterBuilder
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel
     * @param templatePath  模板地址
     * @return {@link ExcelWriterBuilder}
     */
    ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
                                    ResponseExcel responseExcel, String templatePath);

    /**
     * ExcelWriterSheetBuilder 增强
     *
     * @param writerSheetBuilder ExcelWriterSheetBuilder
     * @param sheetNo            sheet角标
     * @param sheetName          sheet名，有模板时为空
     * @param oneRowData         当前写入的首行数据
     * @param template           模板文件
     * @param headEnhancerClass  当前指定的自定义头处理器
     * @return {@link ExcelWriterSheetBuilder}
     */
    ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo, String sheetName,
                                         Object oneRowData, String template, Class<? extends HeadGenerator> headEnhancerClass);

}
