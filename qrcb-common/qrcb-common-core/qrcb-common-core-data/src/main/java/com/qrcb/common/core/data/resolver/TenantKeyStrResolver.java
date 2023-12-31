package com.qrcb.common.core.data.resolver;

import com.qrcb.common.core.assemble.util.KeyStrResolver;
import com.qrcb.common.core.data.tenant.TenantContextHolder;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 租户字符串处理（方便其他模块获取） <br/>
 */

public class TenantKeyStrResolver implements KeyStrResolver {

    /**
     * 传入字符串增加 租户编号:in
     *
     * @param in    输入字符串
     * @param split 分割符
     * @return String
     */
    @Override
    public String extract(String in, String split) {
        return TenantContextHolder.getTenantId() + split + in;
    }

    /**
     * 返回当前租户ID
     *
     * @return String
     */
    @Override
    public String key() {
        return String.valueOf(TenantContextHolder.getTenantId());
    }

}
