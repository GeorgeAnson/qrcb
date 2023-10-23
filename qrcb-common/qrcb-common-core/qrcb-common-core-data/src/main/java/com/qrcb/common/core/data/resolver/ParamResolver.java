package com.qrcb.common.core.data.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.qrcb.admin.api.feign.RemoteParamService;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.util.SpringContextHolder;
import lombok.experimental.UtilityClass;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 系统参数配置解析器 <br/>
 */

@UtilityClass
public class ParamResolver {


    /**
     * 根据key 查询value 配置
     *
     * @param key        key
     * @param defaultVal 默认值
     * @return value
     */
    public Integer getInt(String key, Integer... defaultVal) {
        return checkAndGet(key, Integer.class, defaultVal);
    }

    /**
     * 根据key 查询value 配置
     *
     * @param key        key
     * @param defaultVal 默认值
     * @return value
     */
    public String getStr(String key, String... defaultVal) {
        return checkAndGet(key, String.class, defaultVal);
    }

    private <T> T checkAndGet(String key, Class<T> clazz, T... defaultVal) {
        // 校验入参是否合法
        if (StrUtil.isBlank(key) || defaultVal.length > 1) {
            throw new IllegalArgumentException("参数不合法");
        }

        RemoteParamService remoteParamService = SpringContextHolder.getBean(RemoteParamService.class);

        String result = remoteParamService.getByKey(key, SecurityConstants.FROM_IN).getData();

        if (StrUtil.isNotBlank(result)) {
            return Convert.convert(clazz, result);
        }

        if (defaultVal.length == 1) {
            return Convert.convert(clazz, defaultVal.clone()[0]);

        }
        return null;
    }

}
