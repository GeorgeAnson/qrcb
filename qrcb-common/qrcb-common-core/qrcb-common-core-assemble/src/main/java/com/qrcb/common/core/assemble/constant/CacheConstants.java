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
}
