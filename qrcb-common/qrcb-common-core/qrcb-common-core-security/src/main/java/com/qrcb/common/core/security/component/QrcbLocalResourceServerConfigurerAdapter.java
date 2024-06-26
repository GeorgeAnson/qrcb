package com.qrcb.common.core.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description 持本地模式 [不仅过认证中 CheckToken]的的资源服务器配置 <br/>
 */

@Slf4j
public class QrcbLocalResourceServerConfigurerAdapter  extends ResourceServerConfigurerAdapter {

    @Autowired
    protected AuthenticationEntryPoint resourceAuthExceptionEntryPoint;

    @Autowired
    private PermitAllUrlResolver permitAllUrlResolver;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

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
        resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint).tokenExtractor(tokenExtractor)
                .tokenServices(resourceServerTokenServices);
    }

}
