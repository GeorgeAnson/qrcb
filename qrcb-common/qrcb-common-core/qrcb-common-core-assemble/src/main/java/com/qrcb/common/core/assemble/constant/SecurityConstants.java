package com.qrcb.common.core.assemble.constant;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description <br/>
 */

public interface SecurityConstants {

    /**
     * 启动时是否检查Inner注解安全性
     */
    boolean INNER_CHECK = true;

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * 前缀
     */
    String QRCB_PREFIX = "qrcb_";

    /**
     * 自定义登录URL
     */
    String MOBILE_TOKEN_URL = "/mobile/token/*";

    /**
     * 前缀
     */
    String QRCB_PREFIX = "qrcb_";

    /**
     * oauth 相关前缀
     */
    String OAUTH_PREFIX = "oauth:";

    /**
     * 授权码模式code key 前缀
     */
    String OAUTH_CODE_PREFIX = "oauth:code:";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "id";

    /**
     * 用户名
     */
    String DETAILS_USERNAME = "username";

    /**
     * 用户基本信息
     */
    String DETAILS_USER = "user_info";

    /**
     * 用户名phone
     */
    String DETAILS_PHONE = "phone";

    /**
     * 头像
     */
    String DETAILS_AVATAR = "avatar";

    /**
     * 用户部门字段
     */
    String DETAILS_DEPT_ID = "deptId";

    /**
     * 租户ID 字段
     */
    String DETAILS_TENANT_ID = "tenantId";

    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";

    /**
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 项目的 license
     */
    String QRCB_LICENSE = "made by qrcb";

    /**
     * 激活字段 兼容外围系统接入
     */
    String ACTIVE = "active";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";
}
