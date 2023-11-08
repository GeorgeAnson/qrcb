package com.qrcb.common.core.assemble.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 社交登录类型 <br/>
 */

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    /**
     * 账号密码登录
     */
    PWD("PWD", "账号密码登录"),

    /**
     * 验证码登录
     */
    SMS("SMS", "验证码登录"),

    /**
     * QQ登录
     */
    QQ("QQ", "QQ登录"),

    /**
     * 微信登录
     */
    WECHAT("WX", "微信登录"),

    /**
     * 微信小程序
     */
    MINI_APP("MINI", "微信小程序"),

    /**
     * 码云登录
     */
    GITEE("GITEE", "码云登录"),

    /**
     * 开源中国登录
     */
    OSC("OSC", "开源中国登录");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

}