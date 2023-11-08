package com.qrcb.admin.handler;

import com.qrcb.admin.api.dto.UserInfo;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description <br/>
 */

public abstract class AbstractLoginHandler implements LoginHandler {

    /***
     * 数据合法性校验,默认不校验
     *
     * @param loginStr 通过用户传入获取唯一标识
     * @return {@link Boolean}
     */
    @Override
    public Boolean check(String loginStr) {
        return true;
    }

    /**
     * 处理方法
     *
     * @param loginStr 登录参数
     * @return {@link UserInfo}
     */
    @Override
    public UserInfo handle(String loginStr) {
        if (!check(loginStr)) {
            return null;
        }

        String identify = identify(loginStr);
        return info(identify);
    }
}
