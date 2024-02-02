package com.qrcb.common.extension.excel;

import com.qrcb.common.extension.excel.aop.DynamicNameAspect;
import com.qrcb.common.extension.excel.aop.RequestExcelArgumentResolver;
import com.qrcb.common.extension.excel.aop.ResponseExcelReturnValueHandler;
import com.qrcb.common.extension.excel.header.EmptyHeadGenerator;
import com.qrcb.common.extension.excel.processor.NameProcessor;
import com.qrcb.common.extension.excel.processor.NameSpelExpressionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description Excel 导出配置 <br/>
 */

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
@Import(ExcelHandlerConfiguration.class)
public class ResponseExcelAutoConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    /**
     * SpEL 解析处理器
     *
     * @return NameProcessor excel名称解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public NameProcessor nameProcessor() {
        return new NameSpelExpressionProcessor();
    }

    /**
     * Excel名称解析处理切面
     *
     * @param nameProcessor SpEL 解析处理器
     * @return {@link DynamicNameAspect}
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
        return new DynamicNameAspect(nameProcessor);
    }

    /**
     * 追加 Excel返回值处理器 到 springmvc 中
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
                .getReturnValueHandlers();

        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(responseExcelReturnValueHandler);
        assert returnValueHandlers != null;
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

    /**
     * 空的 Excel 头生成器
     *
     * @return {@link EmptyHeadGenerator}
     */
    @Bean
    @ConditionalOnMissingBean
    public EmptyHeadGenerator emptyHeadGenerator() {
        return new EmptyHeadGenerator();
    }

    /**
     * 追加 Excel 请求处理器 到 springmvc 中
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        resolverList.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }
}
