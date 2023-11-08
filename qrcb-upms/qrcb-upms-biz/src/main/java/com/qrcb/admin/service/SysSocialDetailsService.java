package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysSocialDetails;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 系统社交登录账号表 <br/>
 */

public interface SysSocialDetailsService extends IService<SysSocialDetails> {

    /**
     * 绑定社交账号
     *
     * @param state 类型
     * @param code  code
     * @return {@link Boolean}
     */
    Boolean bindSocial(String state, String code);

    /**
     * 根据入参查询用户信息
     *
     * @param inStr type@code
     * @return {@link UserInfo}
     */
    UserInfo getUserInfo(String inStr);

}
