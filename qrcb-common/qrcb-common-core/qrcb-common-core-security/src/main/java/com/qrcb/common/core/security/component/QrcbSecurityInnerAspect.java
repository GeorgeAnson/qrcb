package com.qrcb.common.core.security.component;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.security.annotation.Inner;
import com.qrcb.common.core.security.util.QrcbSecurityMessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 服务间接口不鉴权处理逻辑 <br/>
 */

@Slf4j
@Aspect
@AllArgsConstructor
public class QrcbSecurityInnerAspect {

    private final HttpServletRequest request;

    @SneakyThrows
    @Around("@within(inner) || @annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        // 先判断 inner 是否为空, 为空则获取类上注解
        if (inner == null) {
            Class<?> aClass = point.getTarget().getClass();
            inner = AnnotationUtils.findAnnotation(aClass, Inner.class);
        }

        String header = request.getHeader(SecurityConstants.FROM);
        if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限", inner.value());
            throw new AccessDeniedException(QrcbSecurityMessageSourceUtil.getAccessor().getMessage(
                    "AbstractAccessDecisionManager.accessDenied", new Object[] { inner.value() }, "access denied"));
        }
        return point.proceed();
    }

}
