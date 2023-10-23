package com.qrcb.common.core.log.event;

import com.qrcb.admin.api.dto.SysLogDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 系统日志事件 <br/>
 */

@Getter
@AllArgsConstructor
public class SysLogEvent {

    private final SysLogDto sysLogDto;

}
