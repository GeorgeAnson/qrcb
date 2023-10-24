package com.qrcb.common.core.gateway.support;

import org.springframework.context.ApplicationEvent;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 路由初始化事件 <br/>
 */

public class DynamicRouteInitEvent extends ApplicationEvent {

    public DynamicRouteInitEvent(Object source) {
        super(source);
    }

}