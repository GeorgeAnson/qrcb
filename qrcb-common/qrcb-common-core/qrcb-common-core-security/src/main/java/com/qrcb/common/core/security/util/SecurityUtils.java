package com.qrcb.common.core.security.util;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.security.service.QrcbUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description 安全工具类 <br/>
 */

@UtilityClass
public class SecurityUtils {


    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     *
     * @param authentication Authentication
     * @return QrcbUser
     * <p>
     */
    public QrcbUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof QrcbUser) {
            return (QrcbUser) principal;
        }
        return null;
    }

    /**
     * 获取用户
     */
    public QrcbUser getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<Integer> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Integer> roleIds = new ArrayList<>();
        authorities.stream().filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
                    roleIds.add(Integer.parseInt(id));
                });
        return roleIds;
    }

}
