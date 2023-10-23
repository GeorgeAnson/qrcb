package com.qrcb.common.core.log;

import com.qrcb.admin.api.feign.RemoteLogService;
import com.qrcb.common.core.assemble.util.KeyStrResolver;
import com.qrcb.common.core.log.aspect.SysLogAspect;
import com.qrcb.common.core.log.event.SysLogListener;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 日志自动配置 <br/>
 */

@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {

    private final RemoteLogService remoteLogService;

    @Bean
    public SysLogListener sysLogListener() {
        return new SysLogListener(remoteLogService);
    }

    @Bean
    public SysLogAspect sysLogAspect(ApplicationEventPublisher publisher, KeyStrResolver resolver) {
        return new SysLogAspect(publisher, resolver);
    }

}
