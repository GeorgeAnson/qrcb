package com.qrcb.common.core.swagger.support;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.Optional;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description SwaggerUiHandler <br/>
 */

@Slf4j
@Component
@AllArgsConstructor
public class SwaggerUiHandler implements HandlerFunction<ServerResponse> {

    private final UiConfiguration uiConfiguration;

    /**
     * Handle the given request.
     *
     * @param request the request to handler
     * @return the response
     */
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(BodyInserters
                .fromValue(Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build())));
    }

}
