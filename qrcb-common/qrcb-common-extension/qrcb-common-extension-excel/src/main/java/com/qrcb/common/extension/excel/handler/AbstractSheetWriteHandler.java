package com.qrcb.common.extension.excel.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.CollectionRowData;
import com.alibaba.excel.write.metadata.RowData;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.qrcb.common.extension.excel.annotation.ResponseExcel;
import com.qrcb.common.extension.excel.aop.DynamicNameAspect;
import com.qrcb.common.extension.excel.config.ExcelConfigProperties;
import com.qrcb.common.extension.excel.converters.LocalDateStringConverter;
import com.qrcb.common.extension.excel.converters.LocalDateTimeStringConverter;
import com.qrcb.common.extension.excel.enhance.WriterBuilderEnhancer;
import com.qrcb.common.extension.excel.header.HeadGenerator;
import com.qrcb.common.extension.excel.header.HeadMeta;
import com.qrcb.common.extension.excel.header.I18nHeaderCellWriteHandler;
import com.qrcb.common.extension.excel.kit.ExcelException;
import com.qrcb.common.extension.excel.kit.LinkedHashMapRowData;
import com.qrcb.common.extension.excel.kit.SheetBuildProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author Anson
 * @Create 2024-02-02
 * @Description Excel 文件写入 <br/>
 */

@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

    private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    private final WriterBuilderEnhancer excelWriterBuilderEnhance;

    private ApplicationContext applicationContext;

    @Getter
    @Setter
    @Autowired(required = false)
    private I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

    @Override
    public void check(ResponseExcel responseExcel) {
        if (responseExcel.fill() && !StringUtils.hasText(responseExcel.template())) {
            throw new ExcelException("@ResponseExcel fill 必须配合 template 使用");
        }
    }

    @Override
    @SneakyThrows(UnsupportedEncodingException.class)
    public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
        check(responseExcel);

        //获取表名
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String name = (String) Objects.requireNonNull(requestAttributes).getAttribute(DynamicNameAspect.EXCEL_NAME_KEY,
                RequestAttributes.SCOPE_REQUEST);
        if (StrUtil.isEmpty(name)) {
            name = UUID.randomUUID().toString();
        }
        String fileName = String.format("%s%s", URLEncoder.encode(name, CharsetUtil.UTF_8), responseExcel.suffix().getValue());
        // 根据实际的文件类型找到对应的 contentType
        String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString)
                .orElse("application/vnd.ms-excel");

        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        write(o, response, responseExcel);
    }


    /**
     * 通用的获取ExcelWriter方法
     *
     * @param response      HttpServletResponse
     * @param responseExcel ResponseExcel注解
     * @return ExcelWriter
     */
    @SneakyThrows(IOException.class)
    public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
                .registerConverter(new LocalDateStringConverter())
                .registerConverter(new LocalDateTimeStringConverter())
                .autoCloseStream(true)
                .excelType(responseExcel.suffix())
                .inMemory(responseExcel.inMemory());

        if (StringUtils.hasText(responseExcel.password())) {
            writerBuilder.password(responseExcel.password());
        }

        if (responseExcel.include().length != 0) {
            writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
        }

        if (responseExcel.exclude().length != 0) {
            writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
        }

        if (responseExcel.writeHandler().length != 0) {
            for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
                writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
            }
        }

        // 开启国际化头信息处理
        if (responseExcel.i18nHeader() && i18nHeaderCellWriteHandler != null) {
            writerBuilder.registerWriteHandler(i18nHeaderCellWriteHandler);
        }

        // 自定义注入的转换器
        registerCustomConverter(writerBuilder);

        if (responseExcel.converter().length != 0) {
            for (Class<? extends Converter> clazz : responseExcel.converter()) {
                writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
            }
        }

        String templatePath = configProperties.getTemplatePath();
        if (StringUtils.hasText(responseExcel.template())) {
            ClassPathResource classPathResource = new ClassPathResource(
                    templatePath + File.separator + responseExcel.template());
            InputStream inputStream = classPathResource.getInputStream();
            writerBuilder.withTemplate(inputStream);
        }

        writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

        return writerBuilder.build();
    }

    /**
     * 自定义注入转换器 如果有需要，子类自己重写
     *
     * @param builder ExcelWriterBuilder
     */
    public void registerCustomConverter(ExcelWriterBuilder builder) {
        converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
    }

    /**
     * 构建一个 空的 WriteSheet 对象
     *
     * @param sheetBuildProperties sheet build 属性
     * @param template             模板信息
     * @return WriteSheet
     */
    public WriteSheet emptySheet(SheetBuildProperties sheetBuildProperties, String template) {
        // Sheet 编号和名称
        Integer sheetNo = sheetBuildProperties.getSheetNo() >= 0 ? sheetBuildProperties.getSheetNo() : null;
        String sheetName = sheetBuildProperties.getSheetName();

        // 是否模板写入
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
                : EasyExcel.writerSheet(sheetNo, sheetName);

        return writerSheetBuilder.build();
    }

    /**
     * 获取 WriteSheet 对象
     *
     * @param sheetBuildProperties  sheet annotation info
     * @param oneRowData            首行数据
     * @param template              模板
     * @param bookHeadEnhancerClass 自定义头处理器
     * @return WriteSheet
     */
    public WriteSheet emptySheet(SheetBuildProperties sheetBuildProperties, Object oneRowData, String template,
                                 Class<? extends HeadGenerator> bookHeadEnhancerClass) {

        // Sheet 编号和名称
        Integer sheetNo = sheetBuildProperties.getSheetNo() >= 0 ? sheetBuildProperties.getSheetNo() : null;
        String sheetName = sheetBuildProperties.getSheetName();

        // 是否模板写入
        ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
                : EasyExcel.writerSheet(sheetNo, sheetName);

        // 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
        Class<? extends HeadGenerator> headGenerateClass = null;
        if (isNotInterface(sheetBuildProperties.getHeadGenerateClass())) {
            headGenerateClass = sheetBuildProperties.getHeadGenerateClass();
        } else if (isNotInterface(bookHeadEnhancerClass)) {
            headGenerateClass = bookHeadEnhancerClass;
        }
        // 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
        if (headGenerateClass != null) {
            fillCustomHeadInfo(oneRowData, bookHeadEnhancerClass, writerSheetBuilder);
        } else if (oneRowData != null) {
            if (oneRowData instanceof Collection<?>) {
                //集合元素类型是基本类型
                writerSheetBuilder.head(genSheetHeader(new CollectionRowData((Collection<?>) oneRowData)));
            } else if (oneRowData instanceof Map) {
                //Map元素类型是K(String),V<Object>类型，默认Key为header
                writerSheetBuilder.head(genSheetHeader(new LinkedHashMapRowData((Map<String, ?>) oneRowData).getKeyRowData()));
            } else {
                writerSheetBuilder.head(oneRowData.getClass());
            }
            if (sheetBuildProperties.getExcludes().length > 0) {
                writerSheetBuilder.excludeColumnFiledNames(Arrays.asList(sheetBuildProperties.getExcludes()));
            }
            if (sheetBuildProperties.getIncludes().length > 0) {
                writerSheetBuilder.includeColumnFiledNames(Arrays.asList(sheetBuildProperties.getIncludes()));
            }
        }

        // sheetBuilder 增强
        writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, oneRowData,
                template, headGenerateClass);

        return writerSheetBuilder.build();
    }

    /**
     * 填写Excel内容
     * @param responseExcel Excel写注解
     * @param eleList 需写入的数据
     * @param excelWriter {@link ExcelWriter}
     * @param sheet {@link WriteSheet}
     * @return {@link ExcelWriter}
     */
    public ExcelWriter writeData(ResponseExcel responseExcel, List<?> eleList, ExcelWriter excelWriter, WriteSheet sheet) {
        if (responseExcel.fill()) {
            // 填充 sheet
            excelWriter.fill(eleList, sheet);
        } else {
            // 写入 sheet
            Object data = eleList.get(0);
            if (data instanceof Map) {
                List<Object> dataList = new ArrayList<>();
                for (Object o : eleList) {
                    LinkedHashMapRowData oneRowData = new LinkedHashMapRowData((Map<String, Object>) o);
                    dataList.add(oneRowData.getValueList());
                }
                excelWriter.write(dataList, sheet);
            } else {
                excelWriter.write(eleList, sheet);
            }
        }
        return excelWriter;
    }

    private List<List<String>> genSheetHeader(RowData oneRowData) {
        List<List<String>> headerList = new ArrayList<>();
        for (int dataIndex = 0; dataIndex < oneRowData.size(); dataIndex++) {
            List<String> header = new ArrayList<>();
            header.add(String.valueOf(oneRowData.get(dataIndex)));
            headerList.add(header);
        }
        return headerList;
    }

    private void fillCustomHeadInfo(Object oneRowData, Class<? extends HeadGenerator> headEnhancerClass,
                                    ExcelWriterSheetBuilder writerSheetBuilder) {
        HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
        Assert.notNull(headGenerator, "The header generated bean does not exist.");
        HeadMeta head = headGenerator.head(oneRowData);
        writerSheetBuilder.head(head.getHead());
        writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
    }

    /**
     * 是否为Null Head Generator
     *
     * @param headGeneratorClass 头生成器类型
     * @return true 已指定 false 未指定(默认值)
     */
    private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
        return !Modifier.isInterface(headGeneratorClass.getModifiers());
    }

    @Override
    @SneakyThrows(BeansException.class)
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
