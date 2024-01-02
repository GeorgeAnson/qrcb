package com.qrcb.common.core.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 参考 DruidDataSourceWrapper <br/>
 */

@Data
@Component
@RefreshScope
@ConfigurationProperties("spring.datasource")
public class HikariDataSourceProperties {

    /**
     * 数据源用户名
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;

    /**
     * url
     */
    private String url;

    /**
     * 数据源驱动
     */
    private String driverClassName;


    /**
     * 查询数据源的SQL
     */
    private String queryDsSql = "SELECT * FROM QRCB_CODEGEN.GEN_DATASOURCE_CONF WHERE DEL_FLAG = 0";

    /**
     * DS注解的 AOP 切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
}
