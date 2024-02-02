package com.qrcb.common.extension.excel;

import com.alibaba.excel.converters.Converter;
import com.qrcb.common.extension.excel.aop.ResponseExcelReturnValueHandler;
import com.qrcb.common.extension.excel.config.ExcelConfigProperties;
import com.qrcb.common.extension.excel.enhance.DefaultWriterBuilderEnhancer;
import com.qrcb.common.extension.excel.enhance.WriterBuilderEnhancer;
import com.qrcb.common.extension.excel.handler.ManySheetWriteHandler;
import com.qrcb.common.extension.excel.handler.SheetWriteHandler;
import com.qrcb.common.extension.excel.handler.SingleSheetWriteHandler;
import com.qrcb.common.extension.excel.header.I18nHeaderCellWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description Excel 处理器配置 <br/>
 */

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = ExcelConfigProperties.class)
public class ExcelHandlerConfiguration {

    private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    /**
     * ExcelBuild增强
     *
     * @return {@link DefaultWriterBuilderEnhancer} 默认增强器
     */
    @Bean
    @ConditionalOnMissingBean
    public WriterBuilderEnhancer writerBuilderEnhancer() {
        return new DefaultWriterBuilderEnhancer();
    }

    /**
     * 单sheet 写入处理器
     *
     * @return {@link SingleSheetWriteHandler}
     */
    @Bean
    @ConditionalOnMissingBean
    public SingleSheetWriteHandler singleSheetWriteHandler() {
        return new SingleSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
    }

    /**
     * 多sheet 写入处理器
     *
     * @return {@link ManySheetWriteHandler}
     */
    @Bean
    @ConditionalOnMissingBean
    public ManySheetWriteHandler manySheetWriteHandler() {
        return new ManySheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer());
    }

    /**
     * 返回Excel文件的 response 处理器
     *
     * @param sheetWriteHandlerList 页签写入处理器集合
     * @return ResponseExcelReturnValueHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
            List<SheetWriteHandler> sheetWriteHandlerList) {
        return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
    }

    /**
     * excel 头的国际化处理器
     *
     * @param messageSource 国际化源
     */
    @Bean
    @ConditionalOnBean(MessageSource.class)
    @ConditionalOnMissingBean
    public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
        return new I18nHeaderCellWriteHandler(messageSource);
    }
}
