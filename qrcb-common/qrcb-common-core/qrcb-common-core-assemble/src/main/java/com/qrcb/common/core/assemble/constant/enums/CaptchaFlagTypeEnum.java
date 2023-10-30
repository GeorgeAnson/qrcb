package com.qrcb.common.core.assemble.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 验证码状态 <br/>
 */

@Getter
@AllArgsConstructor
public enum CaptchaFlagTypeEnum {


    /**
     * 开启验证码
     */
    ON("1", "开启验证码"),

    /**
     * 关闭验证码
     */
    OFF("0", "关闭验证码");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;
}
