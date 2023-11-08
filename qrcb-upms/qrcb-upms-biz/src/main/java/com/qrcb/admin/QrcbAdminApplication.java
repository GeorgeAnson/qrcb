package com.qrcb.admin;

import com.qrcb.common.core.feign.annotation.EnableQrcbFeignClients;
import com.qrcb.common.core.security.annotation.EnableQrcbResourceServer;
import com.qrcb.common.core.swagger.annotation.EnableQrcbSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 用户统一管理系统 <br/>
 */

@EnableQrcbSwagger2
@EnableQrcbFeignClients
@EnableQrcbResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class QrcbAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcbAdminApplication.class, args);
    }
}
