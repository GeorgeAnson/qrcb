package com.qrcb.admin.api.feign;

import com.qrcb.admin.api.entity.SysTenant;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 租户接口 <br/>
 */

@FeignClient(contextId = "remoteTenantService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteTenantService {

    /**
     * 查询全部有效租户
     *
     * @param from 内部标志
     * @return R {@link SysTenant} List
     */
    @GetMapping("/tenant/list")
    R<List<SysTenant>> list(@RequestHeader(SecurityConstants.FROM) String from);

}
