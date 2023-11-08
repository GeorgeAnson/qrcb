package com.qrcb.admin.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysRouteConf;
import reactor.core.publisher.Mono;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 路由 Service 接口 <br/>
 */

public interface SysRouteConfService extends IService<SysRouteConf> {

    /**
     * 更新路由信息
     *
     * @param routes 路由信息
     * @return {@link Void} Mono
     */
    Mono<Void> updateRoutes(JSONArray routes);
}
