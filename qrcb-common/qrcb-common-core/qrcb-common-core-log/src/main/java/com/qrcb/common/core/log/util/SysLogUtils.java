package com.qrcb.common.core.log.util;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.qrcb.admin.api.dto.SysLogDto;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 系统日志工具类 <br/>
 */

@UtilityClass
public class SysLogUtils {

    public SysLogDto getSysLogDto() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setCreateBy(Objects.requireNonNull(getUsername()));
        sysLogDto.setType(LogTypeEnum.NORMAL.getType());
        sysLogDto.setRemoteAddr(ServletUtil.getClientIP(request));
        sysLogDto.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLogDto.setMethod(request.getMethod());
        sysLogDto.setUserAgent(request.getHeader("user-agent"));
        sysLogDto.setParams(HttpUtil.toParams(request.getParameterMap()));
        sysLogDto.setServiceId(getClientId());
        return sysLogDto;
    }

    public SysLogDto getSysLogDto(HttpServletRequest request, String username) {
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setCreateBy(username);
        sysLogDto.setType(LogTypeEnum.NORMAL.getType());
        sysLogDto.setRemoteAddr(ServletUtil.getClientIP(request));
        sysLogDto.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLogDto.setMethod(request.getMethod());
        sysLogDto.setUserAgent(request.getHeader("user-agent"));
        sysLogDto.setParams(HttpUtil.toParams(request.getParameterMap()));
        sysLogDto.setServiceId(getClientId());
        return sysLogDto;
    }

    /**
     * 获取客户端
     *
     * @return clientId
     */
    private String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            return auth2Authentication.getOAuth2Request().getClientId();
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

}
