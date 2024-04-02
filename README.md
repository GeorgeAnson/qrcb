# English | [简体中文](./README_zhCN.md)


<strong>The latest version: 1.0.3</strong> </br>
<p>
Qrcb is a development framework suitable for the financial industry. It also supports db2, mysql, postgresql, hive and other databases. It builds common basic tools in a plug-in manner to simplify and lower the development threshold of complex projects.
</p>

## Core Dependencies

| Rely                     | Version    |
|--------------------------|------------|
| Spring Boot              | 2.6.13     |
| Spring Boot Admin Client | 2.6.11     |
| Spring Cloud             | 2021.0.5   |
| Spring Cloud Alibaba     | 2021.0.5.0 |
| Mybatis Plus             | 3.5.5      |
| hutool                   | 5.8.20     |




## Module Description

```
qrcb
├── qrcb-auth -- Authorization service
└── qrcb-common -- System public module
     └── qrcb-common-core -- Core public dependency modules
        ├── qrcb-common-assemble -- Public tools collection
        ├── qrcb-common-data -- Caching and data processing packages
        ├── qrcb-common-datasource -- Dynamic data source package
        ├── qrcb-common-feign -- feign extended package
        ├── qrcb-common-gateway -- Gateway integration
        ├── qrcb-common-gray -- Grayscale routing component
        ├── qrcb-common-log -- Log service
        ├── qrcb-common-security -- Security tools
        ├── qrcb-common-sentinel -- Fuse current limiting component
        ├── qrcb-common-swagger -- Interface documentation
     └── qrcb-common-extension -- Common component expansion pack
        ├── qrcb-common-extension-datahub -- datahub component
        ├── qrcb-common-extension-excel -- excel import and export components
        ├── qrcb-common-extension-job -- xxl-job package
        ├── qrcb-common-extension-nacos -- nacos persistence plug-in
        ├── qrcb-common-extension-oss -- aws3 protocol encapsulation
        ├── qrcb-common-extension-sequence -- Announcer component
        ├── qrcb-common-extension-transaction -- Distributed transaction support
        ├── qrcb-common-extension-xss -- XSS policy encapsulation
├── qrcb-dependencies -- Core dependent component version management package
├── qrcb-gateway -- Spring Cloud Gateway
├── qrcb-register -- Nacos Server
└── qrcb-upms -- Universal user rights management module
     └── qrcb-upms-api -- Common user rights management system public api module
     └── qrcb-upms-biz -- General user rights management system business processing module
```


## License

Qrcb is released under Apache License 2.0. Please refer to [License](./LICENSE) for details.

## Links

- [Nacos](https://github.com/alibaba/nacos)
- [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- [Spring-Boot](https://github.com/spring-projects/spring-boot)
- [Spring-Cloud](https://github.com/spring-cloud)
- [Spring-Cloud-Alibaba](https://github.com/alibaba/spring-cloud-alibaba)



## Others

- Any developer interested in getting more involved in nacos-datasource-plugin-extension can make [contributions](https://github.com/GeorgeAnson/nacos-datasource-plugin-extension/pulls)!

- Reach out to me through email **georgeanson.gm@gmail.com**. Any issues or questions are welcomed on [Issues](https://github.com/GeorgeAnson/nacos-datasource-plugin-extension/issues).

- Look forward to your opinions. Response may be late but not denied.
