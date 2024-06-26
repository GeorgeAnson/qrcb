package com.qrcb.common.core.sentinel.handler;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.qrcb.common.core.assemble.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 降级限流策略 <br/>
 */

@Slf4j
public class QrcbUrlBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        log.error("sentinel 降级 资源名称{}", e.getRule().getResource(), e);

        response.setContentType(ContentType.JSON.toString());
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().print(JSONUtil.toJsonStr(R.failed(e.getMessage())));
    }

}
