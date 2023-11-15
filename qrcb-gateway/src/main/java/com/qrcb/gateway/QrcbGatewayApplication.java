package com.qrcb.gateway;

import com.qrcb.common.core.gateway.annotation.EnableQrcbDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 网关应用 <br/>
 */

@EnableQrcbDynamicRoute
@EnableDiscoveryClient
@SpringBootApplication
public class QrcbGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcbGatewayApplication.class, args);
    }
}
