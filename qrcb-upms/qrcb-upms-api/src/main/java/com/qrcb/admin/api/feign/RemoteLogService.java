package com.qrcb.admin.api.feign;

import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 远程日志服务 <br/>
 */

@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteLogService {

    /**
     * 保存日志
     * @param sysLog 日志实体
     * @param from 是否内部调用
     * @return succes、false
     */
    @PostMapping("/log/save")
    R<Boolean> saveLog(@RequestBody SysLogDto sysLog, @RequestHeader(SecurityConstants.FROM) String from);

}