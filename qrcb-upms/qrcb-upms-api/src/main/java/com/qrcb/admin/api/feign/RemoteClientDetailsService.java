package com.qrcb.admin.api.feign;

import com.qrcb.admin.api.entity.SysOauthClientDetails;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description <br/>
 */

@FeignClient(contextId = "remoteClientDetailsService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteClientDetailsService {

    /**
     * 通过clientId 查询客户端信息
     *
     * @param clientId 用户名
     * @param from     调用标志
     * @return R {@link SysOauthClientDetails}
     */
    @GetMapping("/client/getClientDetailsById/{clientId}")
    R<SysOauthClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId,
                                                  @RequestHeader(SecurityConstants.FROM) String from);

}
