package com.qrcb.common.core.assemble.constant;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 缓存 key 常量<br/>
 */

public interface CacheConstants {

    /**
     * 全局缓存，在缓存名称上加上该前缀表示该缓存不区分租户，比如:
     * <p/>
     * {@code @Cacheable(value = CacheConstants.GLOBALLY+CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")}
     */
    String GLOBALLY = "gl:";

    /**
     * 用户信息缓存
     */
    String USER_DETAILS = "user_details";

    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "qrcb_oauth:client:details";

    /**
     * 路由存放
     */
    String ROUTE_KEY = GLOBALLY + "gateway_route_key";

    /**
     * 内存reload 时间
     */
    String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";
}
