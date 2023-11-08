package com.qrcb.admin.handler;

import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysUser;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 登录处理器 <br/>
 */

public interface LoginHandler {

    /***
     * 数据合法性校验
     * @param loginStr 通过用户传入获取唯一标识
     * @return {@link Boolean}
     */
    Boolean check(String loginStr);

    /**
     * 通过用户传入获取唯一标识
     *
     * @param loginStr 登录参数
     * @return {@link String}
     */
    String identify(String loginStr);

    /**
     * 通过 openId 获取用户信息
     *
     * @param identify 唯一身份识别标志
     * @return {@link UserInfo}
     */
    UserInfo info(String identify);

    /**
     * 处理方法
     *
     * @param loginStr 登录参数
     * @return {@link UserInfo}
     */
    UserInfo handle(String loginStr);

    /**
     * 绑定逻辑
     *
     * @param user     用户实体
     * @param identify 渠道返回唯一标识
     * @return {@link Boolean}
     */
    Boolean bind(SysUser user, String identify);

}
