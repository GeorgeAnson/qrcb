package com.qrcb.common.core.assemble.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 字典类型 <br/>
 */

@Getter
@AllArgsConstructor
public enum DictTypeEnum {

    /**
     * 字典类型-系统内置（不可修改）
     */
    SYSTEM("1", "系统内置"),

    /**
     * 字典类型-业务类型
     */
    BIZ("0", "业务类");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;
}
