package com.qrcb.common.core.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 参考 DruidDataSourceWrapper <br/>
 */

@Data
@Component
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceProperties {

    /**
     * 数据源用户名
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;

    /**
     * jdbc url
     */
    private String url;

    /**
     * 数据源驱动
     */
    private String driverClassName;

    /**
     * 查询数据源的SQL
     */
    private String queryDsSql = "select * from gen_datasource_conf where del_flag = 0";

}
