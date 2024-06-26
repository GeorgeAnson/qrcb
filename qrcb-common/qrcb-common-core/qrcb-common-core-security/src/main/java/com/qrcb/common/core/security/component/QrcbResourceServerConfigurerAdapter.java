package com.qrcb.common.core.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description
 * 1. 支持 remoteTokenServices 负载均衡 <br/>
 * 2. 支持获取用户全部信息 <br/>
 */

@Slf4j
public class QrcbResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

    @Autowired
    protected AuthenticationEntryPoint resourceAuthExceptionEntryPoint;

    @Autowired
    protected RemoteTokenServices remoteTokenServices;

    @Autowired
    private PermitAllUrlResolver permitAllUrlResolver;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private RestTemplate lbRestTemplate;

    /**
     * 默认的配置，对外暴露
     * @param httpSecurity
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {
        // 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();
        // 配置对外暴露接口
        permitAllUrlResolver.registry(registry);
        registry.anyRequest().authenticated().and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new QrcbUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        remoteTokenServices.setRestTemplate(lbRestTemplate);
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint).tokenExtractor(tokenExtractor)
                .tokenServices(remoteTokenServices);
    }

}
