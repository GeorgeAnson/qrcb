package com.qrcb.common.core.log.aspect;

import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.common.core.assemble.util.KeyStrResolver;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.log.event.SysLogEvent;
import com.qrcb.common.core.log.util.LogTypeEnum;
import com.qrcb.common.core.log.util.SysLogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 操作日志使用 spring event 异步入库 <br/>
 */

@Slf4j
@Aspect
@RequiredArgsConstructor
public class SysLogAspect {

    private final ApplicationEventPublisher publisher;

    private final KeyStrResolver tenantKeyStrResolver;

    @SneakyThrows
    @Around("@annotation(sysLog)")
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        SysLogDto sysLogDto = SysLogUtils.getSysLogDto();
        sysLogDto.setTitle(sysLog.value());
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj;
        try {
            obj = point.proceed();
        }
        catch (Exception e) {
            sysLogDto.setType(LogTypeEnum.ERROR.getType());
            sysLogDto.setException(e.getMessage());
            throw e;
        }
        finally {
            Long endTime = System.currentTimeMillis();
            sysLogDto.setTime(endTime - startTime);
            sysLogDto.setTenantId(Integer.parseInt(tenantKeyStrResolver.key()));
            publisher.publishEvent(new SysLogEvent(sysLogDto));
        }
        return obj;
    }

}
