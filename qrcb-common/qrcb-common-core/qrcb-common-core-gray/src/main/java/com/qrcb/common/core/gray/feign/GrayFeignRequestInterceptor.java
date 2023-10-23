package com.qrcb.common.core.gray.feign;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.util.WebUtils;
import com.qrcb.common.core.gray.support.NonWebVersionContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description feign 请求 Version 传递 <br/>
 */

@Slf4j
public class GrayFeignRequestInterceptor implements RequestInterceptor {

    /**
     * Called for every request. Add data using methods on the supplied
     * {@link RequestTemplate}.
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        String reqVersion = WebUtils.getRequest() != null ? WebUtils.getRequest().getHeader(CommonConstants.VERSION)
                : NonWebVersionContextHolder.getVersion();

        if (StrUtil.isNotBlank(reqVersion)) {
            log.debug("feign gray add header version :{}", reqVersion);
            template.header(CommonConstants.VERSION, reqVersion);
        }
    }

}
