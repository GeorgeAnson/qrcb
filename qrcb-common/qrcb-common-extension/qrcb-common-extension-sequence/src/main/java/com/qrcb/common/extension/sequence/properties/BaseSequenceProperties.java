package com.qrcb.common.extension.sequence.properties;

import lombok.Data;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 发号器通用属性 <br/>
 */

@Data
public class BaseSequenceProperties {

    /**
     * 获取range步长[可选，默认：1000]
     */
    private int step = 1000;

    /**
     * 序列号分配起始值[可选：默认：0]
     */
    private long stepStart = 0;

    /**
     * 业务名称
     */
    private String bizName = "qrcb";

}
