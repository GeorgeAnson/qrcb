package com.qrcb.gateway.handler;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.assemble.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 验证码生成逻辑处理类 <br/>
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCodeCreateHandler implements HandlerFunction<ServerResponse> {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaType(CommonConstants.IMAGE_CODE_TYPE);
        CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
        ResponseModel responseModel = captchaService.get(vo);

        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(R.ok(responseModel))));
    }

}