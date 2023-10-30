package com.qrcb.auth;

import com.qrcb.common.core.feign.annotation.EnableQrcbFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 认证授权中心 <br/>
 */

@EnableQrcbFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class QrcbAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcbAuthApplication.class, args);
    }
}
