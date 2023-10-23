package com.qrcb.common.core.security.listener;

import com.qrcb.common.core.security.handler.AuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureProviderNotFoundEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 认证失败事件监听器 <br/>
 */

public class AuthenticationFailureEventListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Autowired(required = false)
    private AuthenticationFailureHandler failureHandler;

    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        // 此类型事件不传递处理 #2386
        if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        AuthenticationException authenticationException = event.getException();
        Authentication authentication = (Authentication) event.getSource();

        // 调用自定义业务实现
        if (failureHandler != null) {
            failureHandler.handle(authenticationException, authentication, request, response);
        }
    }

}
