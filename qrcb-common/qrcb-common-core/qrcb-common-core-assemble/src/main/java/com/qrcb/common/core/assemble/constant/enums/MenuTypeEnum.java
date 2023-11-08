package com.qrcb.common.core.assemble.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 菜单类型 <br/>
 */

@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    /**
     * 左侧菜单
     */
    LEFT_MENU("0", "left"),

    /**
     * 顶部菜单
     */
    TOP_MENU("2", "top"),

    /**
     * 按钮
     */
    BUTTON("1", "button");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

}
