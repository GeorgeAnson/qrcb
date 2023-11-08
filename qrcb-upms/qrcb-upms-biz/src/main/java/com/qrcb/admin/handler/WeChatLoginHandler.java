package com.qrcb.admin.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysSocialDetails;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.mapper.SysSocialDetailsMapper;
import com.qrcb.admin.service.SysUserService;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 微信 <br/>
 */

@Slf4j
@Component("WX")
@AllArgsConstructor
public class WeChatLoginHandler extends AbstractLoginHandler {

    private final SysUserService sysUserService;

    private final SysSocialDetailsMapper sysSocialDetailsMapper;

    /**
     * 微信登录传入code
     * <p>
     * 通过 code 调用 qq 获取唯一标识
     *
     * @param code code
     * @return {@link String}
     */
    @Override
    public String identify(String code) {
        SysSocialDetails condition = new SysSocialDetails();
        condition.setType(LoginTypeEnum.WECHAT.getType());
        SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));

        String url = String.format(SecurityConstants.WX_AUTHORIZATION_CODE_URL, socialDetails.getAppId(),
                socialDetails.getAppSecret(), code);
        String result = HttpUtil.get(url);
        log.debug("微信响应报文:{}", result);

        Object obj = JSONUtil.parseObj(result).get("openid");
        return obj.toString();
    }

    /**
     * openId 获取用户信息
     *
     * @param openId Wx openId
     * @return {@link UserInfo}
     */
    @Override
    public UserInfo info(String openId) {
        SysUser user = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getWxOpenid, openId));

        if (user == null) {
            log.info("微信未绑定:{}", openId);
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
        List<SysUser> userList = sysUserService
                .list(Wrappers.<SysUser>query().lambda().eq(SysUser::getWxOpenid, identify));

        // 先把原有绑定关系去除,设置绑定为NULL
        if (CollUtil.isNotEmpty(userList)) {
            SysUser condition = new SysUser();
            condition.setWxOpenid(identify);
            sysUserService.update(condition, Wrappers.<SysUser>lambdaUpdate().set(SysUser::getWxOpenid, null));
            log.info("微信账号 {} 更换账号绑定", identify);
        }

        user.setWxOpenid(identify);
        return sysUserService.updateById(user);
    }

}