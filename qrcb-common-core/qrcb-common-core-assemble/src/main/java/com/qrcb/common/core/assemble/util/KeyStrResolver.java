package com.qrcb.common.core.assemble.util;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 字符串处理，方便其他模块解耦处理 <br/>
 */

public interface KeyStrResolver {

    /**
     * 字符串加工
     *
     * @param in    输入字符串
     * @param split 分割符
     * @return 输出字符串
     */
    String extract(String in, String split);

    /**
     * 字符串获取
     *
     * @return 模块返回字符串
     */
    String key();

}
