package com.qrcb.common.extension.transaction.tx.springcloud.listener;

import com.codingapi.tx.listener.service.InitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description <br/>
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerListener implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    private final InitService initService;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("onApplicationEvent -> onApplicationEvent. " + event.getWebServer());
        this.serverPort = event.getWebServer().getPort();
        initService.start();
    }

    public int getPort() {
        return this.serverPort;
    }

}
