package com.qrcb.common.extension.excel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.qrcb.common.extension.excel.annotation.ResponseExcel;
import com.qrcb.common.extension.excel.annotation.Sheet;
import com.qrcb.common.extension.excel.config.ExcelConfigProperties;
import com.qrcb.common.extension.excel.enhance.WriterBuilderEnhancer;
import com.qrcb.common.extension.excel.kit.ExcelException;
import com.qrcb.common.extension.excel.kit.SheetBuildProperties;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description 处理 单个 Sheet 页面写出 <br/>
 */

public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

    public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
                                   ObjectProvider<List<Converter<?>>> converterProvider,
                                   WriterBuilderEnhancer excelWriterBuilderEnhance) {
        super(configProperties, converterProvider, excelWriterBuilderEnhance);
    }

    /**
     * obj 是List才返回true
     *
     * @param obj 返回对象
     * @return boolean
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty();
        } else {
            throw new ExcelException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> eleList = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        // 获取 Sheet 配置
        SheetBuildProperties sheetBuildProperties;
        Sheet[] sheets = responseExcel.sheets();
        if (sheets != null && sheets.length > 0) {
            sheetBuildProperties = new SheetBuildProperties(sheets[0]);
        } else {
            sheetBuildProperties = new SheetBuildProperties(0);
        }

        // 模板信息
        String template = responseExcel.template();

        // 创建sheet
        WriteSheet sheet;
        if (eleList.isEmpty()) {
            sheet = this.emptySheet(sheetBuildProperties, template);
        } else {
            Object oneRowData = eleList.get(0);
            sheet = this.emptySheet(sheetBuildProperties, oneRowData, template, responseExcel.headGenerator());
        }

        excelWriter = writeData(responseExcel, eleList, excelWriter, sheet);

        excelWriter.finish();
    }

}
