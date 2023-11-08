package com.qrcb.admin.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 短信登录 <br/>
 */

@Slf4j
@Component("SMS")
@AllArgsConstructor
public class SmsLoginHandler extends AbstractLoginHandler {

    private final SysUserService sysUserService;

    /**
     * 验证码登录传入为手机号 不用不处理
     *
     * @param mobile 手机号
     * @return {@link String}
     */
    @Override
    public String identify(String mobile) {
        return mobile;
    }

    /**
     * 通过 mobile 获取用户信息
     *
     * @param identify 唯一身份识别标志
     * @return {@link UserInfo}
     */
    @Override
    public UserInfo info(String identify) {
        SysUser user = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getPhone, identify));

        if (user == null) {
            log.info("手机号未注册:{}", identify);
            return null;
        }
        return sysUserService.findUserInfo(user);
    }

    /**
     * 绑定逻辑
     *
     * @param user     用户实体
     * @param identify 渠道返回唯一标识
     * @return {@link Boolean}
     */
    @Override
    public Boolean bind(SysUser user, String identify) {
        user.setPhone(identify);
        return sysUserService.updateById(user);
    }

}
