package com.alibaba.nacos.config;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 覆盖 nacos 默认配置 <br/>
 */

public interface ConfigConstants {

    /**
     * The System property name of Standalone mode
     */
    String STANDALONE_MODE = "nacos.standalone";

    /**
     * 是否开启认证
     */
    String AUTH_ENABLED = "nacos.core.auth.enabled";

    /**
     * 日志目录
     */
    String LOG_BASEDIR = "server.tomcat.basedir";

}
