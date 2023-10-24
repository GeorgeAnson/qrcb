package com.qrcb.common.extension.xss.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description Xss配置类 <br/>
 */

@Getter
@Setter
@ConfigurationProperties("security.xss")
public class QrcbXssProperties implements InitializingBean {

    /**
     * 开启xss
     */
    private boolean enabled = true;

    /**
     * 拦截的路由，默认拦截 /**
     */
    private List<String> pathPatterns = new ArrayList<>();

    /**
     * 放行的规则，默认为空
     */
    private List<String> excludePatterns = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        if (CollUtil.isEmpty(pathPatterns)) {
            pathPatterns.add("/**");
        }
    }

}
