package com.qrcb.admin.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description <br/>
 */

@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {

    /**
     * 分页查询token 信息
     *
     * @param from   内部调用标志
     * @param params 分页参数
     * @return R {@link Page}
     */
    @PostMapping("/token/page")
    R<Page> getTokenPage(@RequestBody Map<String, Object> params, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 删除token
     *
     * @param from  内部调用标志
     * @param token token
     * @return R {@link Boolean}
     */
    @DeleteMapping("/token/{token}")
    R<Boolean> removeTokenById(@PathVariable("token") String token, @RequestHeader(SecurityConstants.FROM) String from);

}
