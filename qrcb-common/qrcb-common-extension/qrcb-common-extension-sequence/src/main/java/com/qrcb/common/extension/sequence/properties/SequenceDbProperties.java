package com.qrcb.common.extension.sequence.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 发号器 DB 配置属性 <br/>
 */

@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "qrcb.sequence.db")
public class SequenceDbProperties extends BaseSequenceProperties {

    private static final long serialVersionUID = 1L;

    /**
     * 表名称
     */
    private String tableName = "qrcb_sequence";

    /**
     * 重试次数
     */
    private int retryTimes = 1;
}
