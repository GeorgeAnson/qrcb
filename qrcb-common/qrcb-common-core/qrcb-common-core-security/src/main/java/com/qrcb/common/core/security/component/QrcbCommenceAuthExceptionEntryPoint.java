package com.qrcb.common.core.security.component;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.security.util.QrcbSecurityMessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description 异常处理 {@link org.springframework.security.core.AuthenticationException } 不同细化异常处理 <br/>
 */

@Slf4j
@Component
@AllArgsConstructor
public class QrcbCommenceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        R<String> result = new R<>();
        result.setMsg(authException.getMessage());
        result.setData(authException.getMessage());
        result.setCode(R.Constants.FAIL);

        if (authException instanceof CredentialsExpiredException || authException instanceof InsufficientAuthenticationException) {
            String msg = QrcbSecurityMessageSourceUtil.getAccessor().getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", authException.getMessage());
            result.setMsg(msg);
        }

        if (authException instanceof UsernameNotFoundException) {
            String msg = QrcbSecurityMessageSourceUtil.getAccessor().getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", authException.getMessage());
            result.setMsg(msg);
        }

        if (authException instanceof BadCredentialsException) {
            String msg = QrcbSecurityMessageSourceUtil.getAccessor().getMessage("AbstractUserDetailsAuthenticationProvider.badClientCredentials", authException.getMessage());
            result.setMsg(msg);
        }

        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }

}