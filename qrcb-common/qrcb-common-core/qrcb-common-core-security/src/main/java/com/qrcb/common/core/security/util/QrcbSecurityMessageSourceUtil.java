package com.qrcb.common.core.security.util;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description 建议所有异常都使用此工具类型 避免无法复写 SpringSecurityMessageSource
 * @see org.springframework.security.core.SpringSecurityMessageSource 框架自身异常处理 <br/>
 */

public class QrcbSecurityMessageSourceUtil extends ReloadableResourceBundleMessageSource {

    public QrcbSecurityMessageSourceUtil() {
        setBasename("classpath:messages/messages");
        setDefaultLocale(Locale.CHINA);
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new QrcbSecurityMessageSourceUtil());
    }

}