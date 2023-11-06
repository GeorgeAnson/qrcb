package com.alibaba.nacos;

import com.alibaba.nacos.config.ConfigConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 注册中心 <br/>
 */

@EnableScheduling
@SpringBootApplication
public class QrcbNacosApplication {

    public static void main(String[] args) {
        System.setProperty(ConfigConstants.WEB_CONTEXT_PATH, "/nacos");
        System.setProperty(ConfigConstants.STANDALONE_MODE, "true");
        System.setProperty(ConfigConstants.AUTH_ENABLED, "false");
        System.setProperty(ConfigConstants.LOG_BASEDIR, "logs");
        SpringApplication.run(QrcbNacosApplication.class, args);
    }
}
