# [English](./README.md) | 简体中文


<strong>最新版本：1.0.3</strong> </br>
<p>
qrcb是一款适用于金融行业的开发框架，同时支持db2、mysql、postgresql、hive等数据库，以插件化的方式搭建常见基础工具，简化并降低复杂项目的开发门槛。 
</p>

## 核心依赖

| 依赖                       | 版本             |
|--------------------------|----------------|
| Spring Boot              | 2.6.13          |
| Spring Boot Admin Client | 2.6.11          |
| Spring Cloud             | 2021.0.5       |
| Spring Cloud Alibaba     | 2021.0.5.0 |
| Mybatis Plus             | 3.5.5        |
| hutool                   | 5.8.20         |




## 模块说明

```
qrcb
├── qrcb-auth -- 授权服务
└── qrcb-common -- 系统公共模块
     └── qrcb-common-core -- 核心公共依赖模块
        ├── qrcb-common-assemble -- 公共工具集合
        ├── qrcb-common-data -- 缓存及数据处理包
        ├── qrcb-common-datasource -- 动态数据源包
        ├── qrcb-common-feign -- feign 扩展封装
        ├── qrcb-common-gateway -- 网关集成
        ├── qrcb-common-gray -- 灰度路由组件
        ├── qrcb-common-log -- 日志服务
        ├── qrcb-common-security -- 安全工具类
        ├── qrcb-common-sentinel -- 熔断限流组件
        ├── qrcb-common-swagger -- 接口文档
     └── qrcb-common-extension -- 公共组件扩展包
        ├── qrcb-common-extension-datahub -- datahub组件
        ├── qrcb-common-extension-excel -- excel导入导出组件
        ├── qrcb-common-extension-job -- xxl-job封装
        ├── qrcb-common-extension-nacos -- nacos持久化插件
        ├── qrcb-common-extension-oss -- aws3协议封装
        ├── qrcb-common-extension-sequence -- 发号器组件
        ├── qrcb-common-extension-transaction -- 分布式事务支持
        ├── qrcb-common-extension-xss -- XSS 策略封装
     └── qrcb-common-feign -- feign 扩展封装
     └── qrcb-common-xss -- xss 安全封装
├── qrcb-dependencies -- 核心依赖组件版本管理包
├── qrcb-gateway -- Spring Cloud Gateway网关
├── qrcb-register -- Nacos Server
└── qrcb-upms -- 通用用户权限管理模块
     └── qrcb-upms-api -- 通用用户权限管理系统公共api模块
     └── qrcb-upms-biz -- 通用用户权限管理系统业务处理模块
```


## 相关链接

- [Nacos](https://github.com/alibaba/nacos)
- [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- [Spring-Boot](https://github.com/spring-projects/spring-boot)
- [Spring-Cloud](https://github.com/spring-cloud)
- [Spring-Cloud-Alibaba](https://github.com/alibaba/spring-cloud-alibaba)



## 其他
* 开源许可证：Apache License, Version 2.0
* 任何有兴趣更多地参与 qrcb 的开发人员都可以做出[贡献](https://github.com/GeorgeAnson/qrcb/pulls)！
* 通过电子邮件 `georgeanson.gm@gmail.com` 与我联系。如有任何问题或疑问，欢迎在[问题](https://github.com/GeorgeAnson/qrcb/issues)上提出。
* 期待您的意见。回复可能会迟到，但不会被拒绝。