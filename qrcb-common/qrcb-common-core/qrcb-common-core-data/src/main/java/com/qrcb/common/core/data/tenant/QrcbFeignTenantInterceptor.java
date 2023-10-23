package com.qrcb.common.core.data.tenant;

import com.qrcb.common.core.assemble.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description <br/>
 */

@Slf4j
public class QrcbFeignTenantInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (TenantContextHolder.getTenantId() == null) {
            log.debug("TTL 中的 租户ID为空，feign拦截器 >> 跳过");
            return;
        }
        requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId().toString());
    }

}
