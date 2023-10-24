package com.qrcb.common.extension.xss.core;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 利用 ThreadLocal 缓存线程间的数据 <br/>
 */

public class XssHolder {

    private static final ThreadLocal<Boolean> TL = new ThreadLocal<>();

    public static boolean isEnabled() {
        return Boolean.TRUE.equals(TL.get());
    }

    public static void setEnable() {
        TL.set(Boolean.TRUE);
    }

    public static void remove() {
        TL.remove();
    }

}
