package com.qrcb.common.core.datasource.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 数据源配置类型 <br/>
 */

@Getter
@AllArgsConstructor
public enum DsConfTypeEnum {

    /**
     * 主机链接
     */
    HOST(0, "主机链接"),

    /**
     * JDBC链接
     */
    JDBC(1, "JDBC链接");

    private final Integer type;

    private final String description;
}
