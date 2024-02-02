package com.qrcb.common.extension.excel.annotation;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description 导入时候获取行号 <br/>
 */

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelLine {
}
