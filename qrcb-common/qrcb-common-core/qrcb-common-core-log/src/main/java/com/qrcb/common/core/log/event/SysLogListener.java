package com.qrcb.common.core.log.event;

import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.admin.api.feign.RemoteLogService;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 异步监听日志事件 <br/>
 */

@Slf4j
@AllArgsConstructor
public class SysLogListener {


    private final RemoteLogService remoteLogService;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLogDto sysLogDto = event.getSysLogDto();
        remoteLogService.saveLog(sysLogDto, SecurityConstants.FROM_IN);
    }

}
