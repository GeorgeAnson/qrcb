package com.qrcb.common.core.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description token 发放失败处理 <br/>
 */

public interface AuthenticationFailureHandler {

    /**
     * 业务处理
     * @param authenticationException 错误信息
     * @param authentication 认证信息
     * @param request 请求信息
     * @param response 响应信息
     */
    void handle(AuthenticationException authenticationException, Authentication authentication,
                HttpServletRequest request, HttpServletResponse response);

}
