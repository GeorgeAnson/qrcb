package com.qrcb.common.extension.excel.annotation;

import com.qrcb.common.extension.excel.header.HeadGenerator;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel sheet 页注解 <br/>
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

    int sheetNo() default -1;

    /**
     * sheet name
     */
    String sheetName();

    /**
     * 包含字段
     */
    String[] includes() default {};

    /**
     * 排除字段
     */
    String[] excludes() default {};

    /**
     * 头生成器
     */
    Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
