package com.qrcb.admin.api.feign;

import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 查询参数相关 <br/>
 */

@FeignClient(contextId = "remoteParamService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteParamService {

    /**
     * 通过key 查询参数配置
     *
     * @param key  key
     * @param from 声明成内部调用，避免MQ 等无法调用
     * @return
     */
    @GetMapping("/param/publicValue/{key}")
    R<String> getByKey(@PathVariable("key") String key, @RequestHeader(SecurityConstants.FROM) String from);

}
