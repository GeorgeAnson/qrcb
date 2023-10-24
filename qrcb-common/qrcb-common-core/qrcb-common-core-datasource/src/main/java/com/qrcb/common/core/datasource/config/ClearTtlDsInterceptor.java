package com.qrcb.common.core.datasource.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 清空上文的 DS 设置避免污染当前线程 <br/>
 */

public class ClearTtlDsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        DynamicDataSourceContextHolder.clear();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        DynamicDataSourceContextHolder.clear();
    }

}