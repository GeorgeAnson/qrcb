package com.qrcb.common.core.gateway.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 扩展此类支持序列化 <br/>
 * @see RouteDefinition
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class RouteDefinitionVo extends RouteDefinition implements Serializable {

    /**
     * 路由名称
     */
    private String routeName;

}