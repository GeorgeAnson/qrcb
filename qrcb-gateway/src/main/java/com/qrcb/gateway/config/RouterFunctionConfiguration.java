package com.qrcb.gateway.config;

import com.qrcb.gateway.handler.ImageCodeCheckHandler;
import com.qrcb.gateway.handler.ImageCodeCreateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 路由配置信息 <br/>
 */

@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

    private final ImageCodeCheckHandler imageCodeCheckHandler;

    private final ImageCodeCreateHandler imageCodeCreateHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions
                .route(RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        imageCodeCreateHandler)
                .andRoute(RequestPredicates.POST("/code/check").and(RequestPredicates.accept(MediaType.ALL)),
                        imageCodeCheckHandler);

    }

}
