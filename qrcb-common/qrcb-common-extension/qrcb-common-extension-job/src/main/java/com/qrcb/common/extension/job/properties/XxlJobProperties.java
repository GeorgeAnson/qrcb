package com.qrcb.common.extension.job.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description xxl-job 配置 <br/>
 */

@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties implements InitializingBean, EnvironmentAware {

    private Environment environment;

    private XxlAdminProperties admin = new XxlAdminProperties();

    private XxlExecutorProperties executor = new XxlExecutorProperties();

    @Override
    public void afterPropertiesSet() {
        // 若是没有设置 appName 则取 application Name
        if (!StringUtils.hasText(executor.getAppName())) {
            executor.setAppName(environment.getProperty("spring.application.name"));
        }
    }

    /**
     * Set the {@code Environment} that this component runs in.
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
