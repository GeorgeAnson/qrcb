package com.qrcb.common.core.gateway.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description Ribbon 配置 <br/>
 */

@Getter
@Setter
@RefreshScope
@ConfigurationProperties("ribbon.rule")
public class QrcbRibbonRuleProperties {


    /**
     * 是否开启，默认：true
     */
    private boolean enabled = Boolean.TRUE;

    /**
     * 是否开启灰度路由，默认:false
     */
    private boolean grayEnabled;

    /**
     * 优先的ip列表，支持通配符，例如：10.20.0.8*、10.20.0.*
     */
    private List<String> priorIpPattern = new ArrayList<>();

}
