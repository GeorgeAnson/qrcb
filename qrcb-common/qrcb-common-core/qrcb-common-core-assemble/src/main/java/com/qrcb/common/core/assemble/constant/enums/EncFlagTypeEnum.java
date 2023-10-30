package com.qrcb.common.core.assemble.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 密码是否加密传输 <br/>
 */

@Getter
@AllArgsConstructor
public enum EncFlagTypeEnum {

    /**
     * 是
     */
    YES("1", "是"),

    /**
     * 否
     */
    NO("0", "否");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;
}
