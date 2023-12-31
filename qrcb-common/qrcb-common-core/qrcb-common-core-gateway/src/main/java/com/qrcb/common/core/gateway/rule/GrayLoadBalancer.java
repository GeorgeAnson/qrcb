package com.qrcb.common.core.gateway.rule;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 灰度路由 <br/>
 */

public interface GrayLoadBalancer {

    /**
     * 根据serviceId 筛选可用服务
     *
     * @param serviceId 服务ID
     * @param request   当前请求
     * @return {@link ServiceInstance}
     */
    ServiceInstance choose(String serviceId, ServerHttpRequest request);

}