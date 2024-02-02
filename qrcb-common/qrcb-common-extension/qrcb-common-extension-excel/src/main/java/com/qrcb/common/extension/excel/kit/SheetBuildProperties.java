package com.qrcb.common.extension.excel.kit;

import com.qrcb.common.extension.excel.annotation.Sheet;
import com.qrcb.common.extension.excel.header.HeadGenerator;
import lombok.Data;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description <br/>
 */

@Data
public class SheetBuildProperties {

    /**
     * sheet 编号
     */
    private int sheetNo = -1;

    /**
     * sheet name
     */
    private String sheetName;

    /**
     * 包含字段
     */
    private String[] includes = new String[0];

    /**
     * 排除字段
     */
    private String[] excludes = new String[0];

    /**
     * 头生成器
     */
    private Class<? extends HeadGenerator> headGenerateClass = HeadGenerator.class;

    public SheetBuildProperties(Sheet sheetAnnotation) {
        this.sheetNo = sheetAnnotation.sheetNo();
        this.sheetName = sheetAnnotation.sheetName();
        this.includes = sheetAnnotation.includes();
        this.excludes = sheetAnnotation.excludes();
        this.headGenerateClass = sheetAnnotation.headGenerateClass();
    }

    public SheetBuildProperties(int index) {
        this.sheetNo = index;
        this.sheetName = "sheet" + (sheetNo + 1);
    }
}
