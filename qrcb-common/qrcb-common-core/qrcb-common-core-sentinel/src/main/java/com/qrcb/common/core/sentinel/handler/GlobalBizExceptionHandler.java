package com.qrcb.common.core.sentinel.handler;

import com.alibaba.csp.sentinel.Tracer;
import com.qrcb.common.core.assemble.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 全局异常处理器结合 sentinel 全局异常处理器不能作用在 oauth server <br/>
 */

@Slf4j
@RestController
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalBizExceptionHandler {

    /**
     * 全局异常
     *
     * @param e Exception
     * @return {@link String} R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        // 业务异常交由 sentinel 记录
        Tracer.trace(e);
        return R.failed(e.getLocalizedMessage());
    }

    /**
     * AccessDeniedException
     *
     * @param e AccessDeniedException
     * @return {@link String} R
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("拒绝授权异常信息 ex={}", e.getMessage());
        return R.failed("权限不足，不允许访问");
    }

    /**
     * validation Exception
     *
     * @param exception MethodArgumentNotValidException
     * @return {@link String} R
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> handleBodyValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(fieldErrors.get(0).getDefaultMessage());
    }

    /**
     * 避免 404 重定向到 /error 导致 NPE ,ignore-url 需要配置对应端点
     *
     * @return {@link String} R
     */
    @DeleteMapping("/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<String> noHandlerFoundException() {
        return R.failed(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
