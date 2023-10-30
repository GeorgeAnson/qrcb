package com.qrcb.auth.handler;

import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.admin.api.feign.RemoteLogService;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.util.KeyStrResolver;
import com.qrcb.common.core.assemble.util.WebUtils;
import com.qrcb.common.core.log.util.SysLogUtils;
import com.qrcb.common.core.security.handler.AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description <br/>
 */

@Slf4j
@Component
@AllArgsConstructor
public class QrcbAuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

    private final RemoteLogService logService;

    private final KeyStrResolver tenantKeyStrResolver;

    /**
     * 处理登录成功方法
     * <p>
     * 获取到登录的 authentication 对象
     *
     * @param authentication 登录对象
     * @param request        请求
     * @param response       返回
     */
    @Async
    @Override
    public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String username = authentication.getName();
        SysLogDto sysLog = SysLogUtils.getSysLogDto(request, username);
        sysLog.setTitle(username + "用户登录");
        sysLog.setParams(username);
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        sysLog.setServiceId(WebUtils.extractClientId(header).orElse("N/A"));
        sysLog.setTenantId(Integer.parseInt(tenantKeyStrResolver.key()));

        logService.saveLog(sysLog, SecurityConstants.FROM_IN);
        log.info("用户：{} 登录成功", username);
    }
}
