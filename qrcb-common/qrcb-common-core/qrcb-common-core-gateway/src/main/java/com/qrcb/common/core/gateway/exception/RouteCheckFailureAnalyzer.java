package com.qrcb.common.core.gateway.exception;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 格式化异常信息，方便启动时观察。 <br/>
 */

public class RouteCheckFailureAnalyzer extends AbstractFailureAnalyzer<RouteCheckException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RouteCheckException cause) {

        return new FailureAnalysis(cause.getMessage(),
                "检查：1. redis 是否正常启动并连接；2.应用启动顺序；3. DB 中是否有路由信息。", cause);
    }

}
