package com.qrcb.common.core.data.tenant;

import com.qrcb.common.core.assemble.constant.CommonConstants;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 传递 RestTemplate 请求的租户ID <br/>
 */

public class TenantRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        if (TenantContextHolder.getTenantId() != null) {
            request.getHeaders().set(CommonConstants.TENANT_ID, String.valueOf(TenantContextHolder.getTenantId()));
        }

        return execution.execute(request, body);
    }

}
