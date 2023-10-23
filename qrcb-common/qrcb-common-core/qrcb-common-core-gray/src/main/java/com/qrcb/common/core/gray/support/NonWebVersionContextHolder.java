package com.qrcb.common.core.gray.support;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 灰度版本号传递工具 ,在非 web 调用 feign 传递之前手动 setVersion <br/>
 */

@UtilityClass
public class NonWebVersionContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_VERSION = new TransmittableThreadLocal<>();

    /**
     * TTL 设置版本号<br/>
     *
     * @param version 版本号
     */
    public void setVersion(String version) {
        THREAD_LOCAL_VERSION.set(version);
    }

    /**
     * 获取TTL中的版本号
     *
     * @return 版本 || null
     */
    public String getVersion() {
        return THREAD_LOCAL_VERSION.get();
    }

    public void clear() {
        THREAD_LOCAL_VERSION.remove();
    }

}
