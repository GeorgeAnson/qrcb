package com.qrcb.common.core.security.annotation;

import com.qrcb.common.core.security.component.QrcbResourceServerAutoConfiguration;
import com.qrcb.common.core.security.component.QrcbSecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 资源服务注解 <br/>
 */

@Documented
@Inherited
@EnableResourceServer
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ QrcbResourceServerAutoConfiguration.class, QrcbSecurityBeanDefinitionRegistrar.class })
public @interface EnableQrcbResourceServer {

    /**
     * 是否开启本地模式
     * @return true
     */
    boolean isLocal() default true;
}
