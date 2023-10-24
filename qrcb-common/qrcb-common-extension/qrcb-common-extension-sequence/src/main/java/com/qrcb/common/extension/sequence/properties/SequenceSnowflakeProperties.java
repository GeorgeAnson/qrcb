package com.qrcb.common.extension.sequence.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description Snowflake 发号器属性 <br/>
 */

@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "qrcb.sequence.snowflake")
public class SequenceSnowflakeProperties extends BaseSequenceProperties {

    private static final long serialVersionUID = 1L;

    /**
     * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
     */
    private long dataCenterId;

    /**
     * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
     */
    private long workerId;

}