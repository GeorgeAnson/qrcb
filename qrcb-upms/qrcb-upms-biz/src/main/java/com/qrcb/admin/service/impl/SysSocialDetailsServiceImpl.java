package com.qrcb.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysSocialDetails;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.handler.LoginHandler;
import com.qrcb.admin.mapper.SysSocialDetailsMapper;
import com.qrcb.admin.mapper.SysUserMapper;
import com.qrcb.admin.service.SysSocialDetailsService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 系统社交登录账号表 <br/>
 */

@Slf4j
@Service
@AllArgsConstructor
public class SysSocialDetailsServiceImpl extends ServiceImpl<SysSocialDetailsMapper, SysSocialDetails>
        implements SysSocialDetailsService {

    private final Map<String, LoginHandler> loginHandlerMap;

    private final CacheManager cacheManager;

    private final SysUserMapper sysUserMapper;

    @Override
    public Boolean bindSocial(String type, String code) {
        LoginHandler loginHandler = loginHandlerMap.get(type);
        // 绑定逻辑
        String identify = loginHandler.identify(code);
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getUser().getId());
        loginHandler.bind(sysUser, identify);

        // 更新緩存
        cacheManager.getCache(CacheConstants.USER_DETAILS).evict(sysUser.getUsername());
        return Boolean.TRUE;
    }

    @Override
    public UserInfo getUserInfo(String inStr) {
        String[] inStrs = inStr.split(StrUtil.AT);
        String type = inStrs[0];
        String loginStr = inStrs[1];
        return loginHandlerMap.get(type).handle(loginStr);
    }

}
