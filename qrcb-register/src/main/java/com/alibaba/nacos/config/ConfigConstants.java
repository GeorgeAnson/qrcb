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

    String WEB_CONTEXT_PATH="server.servlet.contextPath";

    /**
     * 是否开启认证
     */
    String AUTH_ENABLED = "nacos.core.auth.enabled";

    /**
     * 认证类型：nacos, ldap
     */
    String AUTH_TYPE="nacos.core.auth.system.type";

    /**
     * 服务端身份认证 key
     */
    String SERVER_IDENTITY_KEY="nacos.core.auth.server.identity.key";

    /**
     * 服务端身份认证 value
     */
    String SERVER_IDENTITY_VALUE="nacos.core.auth.server.identity.value";

    /**
     * user-agent，除非nacos升级，否则默认关闭
     */
    String USER_AGENT_AUTH_WHITE="nacos.core.auth.enable.userAgentAuthWhite";
    /**
     * 日志目录
     */
    String LOG_BASEDIR = "server.tomcat.basedir";

    /**
     * access_log日志开关
     */
    String LOG_ENABLED = "server.tomcat.accesslog.enabled";

}
