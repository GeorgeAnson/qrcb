package com.qrcb.common.extension.transaction;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 事物配置 <br/>
 */

@Configuration
@ComponentScan({ "com.codingapi.tx", "com.qrcb.common.extension.transaction" })
public class TransactionConfiguration {
}
