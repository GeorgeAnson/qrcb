package com.qrcb.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcb.common.core.security.component.QrcbCommenceAuthExceptionEntryPoint;
import com.qrcb.common.core.security.component.QrcbWebResponseExceptionTranslator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 认证服务器配置 <br/>
 */

@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ClientDetailsService qrcbClientDetailsServiceImpl;

    private final AuthenticationManager authenticationManagerBean;

    private final UserDetailsService qrcbUserDetailsService;

    private final AuthorizationCodeServices authorizationCodeServices;

    private final TokenStore redisTokenStore;

    private final TokenEnhancer tokenEnhancer;

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(qrcbClientDetailsServiceImpl);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients()
                .authenticationEntryPoint(new QrcbCommenceAuthExceptionEntryPoint(objectMapper))
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenStore(redisTokenStore)
                .tokenEnhancer(tokenEnhancer).userDetailsService(qrcbUserDetailsService)
                .authorizationCodeServices(authorizationCodeServices).authenticationManager(authenticationManagerBean)
                .reuseRefreshTokens(false).pathMapping("/oauth/confirm_access", "/token/confirm_access")
                .exceptionTranslator(new QrcbWebResponseExceptionTranslator());
    }

}
