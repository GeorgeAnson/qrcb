package com.qrcb.common.core.assemble.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.qrcb.common.core.assemble.jackson.QrcbJavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description <br/>
 */

@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JacksonConfig implements WebMvcConfigurer {

    private static final String ASIA_SHANGHAI = "Asia/Shanghai";

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ASIA_SHANGHAI));
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            //Jackson全局转化Long类型为String，解决jackson序列化时传入前端Long类型缺失精度问题
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.modules(new QrcbJavaTimeModule());
        };
    }

    /**
     * 增加GET请求参数中时间类型转换 {@link QrcbJavaTimeModule}
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     *
     * @param registry {@link DateTimeFormatterRegistrar}
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        registrar.registerFormatters(registry);
    }

    /**
     * 避免 form 提交 Context-Type 不规范中文乱码
     *
     * @return {@link OrderedCharacterEncodingFilter}
     */
    @Bean
    public OrderedCharacterEncodingFilter characterEncodingFilter() {
        OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(CharsetUtil.UTF_8);
        filter.setForceEncoding(true);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }
}
