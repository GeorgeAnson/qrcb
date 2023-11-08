package com.qrcb.admin.controller;

import cn.hutool.json.JSONArray;
import com.qrcb.admin.api.entity.SysRouteConf;
import com.qrcb.admin.service.SysRouteConfService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 动态路由管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/route")
@Api(value = "route", tags = "动态路由管理模块")
public class SysRouteConfController {


    private final SysRouteConfService sysRouteConfService;

    /**
     * 获取当前定义的路由信息
     *
     * @return {@link SysRouteConf} List
     */
    @GetMapping
    public R<List<SysRouteConf>> listRoutes() {
        return R.ok(sysRouteConfService.list());
    }

    /**
     * 修改路由
     *
     * @param routes 路由定义
     * @return {@link Void} Mono
     */
    @SysLog("修改路由")
    @PutMapping
    public R<Mono<Void>> updateRoutes(@RequestBody JSONArray routes) {
        return R.ok(sysRouteConfService.updateRoutes(routes));
    }

}
