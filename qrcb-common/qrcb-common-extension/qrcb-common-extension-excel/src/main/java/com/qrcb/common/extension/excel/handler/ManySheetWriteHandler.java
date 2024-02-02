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
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description 处理多 Sheet 数据写出 <br/>
 */

public class ManySheetWriteHandler extends AbstractSheetWriteHandler {

    public ManySheetWriteHandler(ExcelConfigProperties configProperties,
                                 ObjectProvider<List<Converter<?>>> converterProvider,
                                 WriterBuilderEnhancer excelWriterBuilderEnhance) {
        super(configProperties, converterProvider, excelWriterBuilderEnhance);
    }

    /**
     * 当且仅当List不为空才返回true
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
        List<?> objList = (List<?>) obj;
        int objListSize = objList.size();

        String template = responseExcel.template();

        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);
        List<SheetBuildProperties> sheetBuildPropertiesList = getSheetBuildProperties(responseExcel, objListSize);

        for (int i = 0; i < sheetBuildPropertiesList.size(); i++) {
            SheetBuildProperties sheetBuildProperties = sheetBuildPropertiesList.get(i);
            // 创建sheet
            WriteSheet sheet;
            List<?> eleList;
            if (objListSize <= i) {
                eleList = new ArrayList<>();
                sheet = this.emptySheet(sheetBuildProperties, template);
            } else {
                eleList = (List<?>) objList.get(i);
                if (eleList.isEmpty()) {
                    sheet = this.emptySheet(sheetBuildProperties, template);
                } else {
                    Object oneRowData = eleList.get(0);
                    sheet = this.emptySheet(sheetBuildProperties, oneRowData, template, responseExcel.headGenerator());
                }
            }

            excelWriter = writeData(responseExcel, eleList, excelWriter, sheet);
        }

        excelWriter.finish();
    }

    private static List<SheetBuildProperties> getSheetBuildProperties(ResponseExcel responseExcel, int objListSize) {
        List<SheetBuildProperties> sheetBuildPropertiesList = new ArrayList<>();
        Sheet[] sheets = responseExcel.sheets();
        if (sheets != null && sheets.length > 0) {
            for (Sheet sheet : sheets) {
                sheetBuildPropertiesList.add(new SheetBuildProperties(sheet));
            }
        } else {
            for (int i = 0; i < objListSize; i++) {
                sheetBuildPropertiesList.add(new SheetBuildProperties(i));
            }
        }
        return sheetBuildPropertiesList;
    }
}
