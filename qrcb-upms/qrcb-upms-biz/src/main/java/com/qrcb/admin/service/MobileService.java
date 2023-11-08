package com.qrcb.admin.service;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 手机验证码 Service 接口 <br/>
 */

public interface MobileService {

    /**
     * 发送手机验证码
     *
     * @param mobile mobile
     * @return {@link String} 验证码
     */
    String sendSmsCode(String mobile);
}
