package com.qrcb.gateway.config;

import com.qrcb.common.core.swagger.config.SwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 聚合接口文档注册，和 zuul 实现类似 <br/>
 */

@Primary
@Component
@Import(SwaggerProperties.class)
@RequiredArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";

    private final RouteDefinitionRepository routeDefinitionRepository;

    private final SwaggerProperties swaggerProperties;

    private final DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        List<RouteDefinition> routes = new ArrayList<>();
        routeDefinitionRepository.getRouteDefinitions()
                // swagger 显示服务名称根据 路由的order 字段进行升序排列
                .sort(Comparator.comparingInt(RouteDefinition::getOrder)).subscribe(routes::add);

        return routes.stream()
                .flatMap(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        .filter(predicateDefinition -> !swaggerProperties.getIgnoreProviders()
                                .contains(routeDefinition.getId()))
                        .map(predicateDefinition -> swaggerResource(routeDefinition.getId(),
                                predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**",
                                        API_URI))))
                // 只显示注册中心 内已经正确启动的服务
                .filter(swaggerResource -> discoveryClient.getServices().stream()
                        .anyMatch(s -> s.equals(swaggerResource.getName())))
                .collect(Collectors.toList());
    }

    private static SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
