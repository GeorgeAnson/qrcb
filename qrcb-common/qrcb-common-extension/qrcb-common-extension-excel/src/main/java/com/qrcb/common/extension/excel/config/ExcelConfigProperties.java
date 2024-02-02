package com.qrcb.common.extension.excel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description 模板文件路径 <br/>
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "excel")
public class ExcelConfigProperties {

    /**
     * 模板路径
     */
    private String templatePath = "excel";
}
