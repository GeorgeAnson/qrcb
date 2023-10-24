package com.qrcb.common.extension.transaction.tx.springcloud.http;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description <br/>
 */

@Slf4j
public class TransactionHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @SneakyThrows
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();

        log.info("LCN-SpringCloud TxGroup info -> groupId:" + groupId);

        if (txTransactionLocal != null) {
            request.getHeaders().add("tx-group", groupId);
        }
        return execution.execute(request, body);
    }

}
