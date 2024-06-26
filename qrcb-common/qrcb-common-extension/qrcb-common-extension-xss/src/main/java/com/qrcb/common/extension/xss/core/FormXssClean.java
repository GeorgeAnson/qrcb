package com.qrcb.common.extension.xss.core;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.extension.xss.util.XssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 表单 xss 处理 <br/>
 */

@ControllerAdvice
public class FormXssClean {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 处理前端传来的表单字符串
        binder.registerCustomEditor(String.class, new StringPropertiesEditor());
    }

    @Slf4j
    public static class StringPropertiesEditor extends PropertyEditorSupport {

        @Override
        public String getAsText() {
            Object value = getValue();
            return value != null ? value.toString() : StrUtil.EMPTY;
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null) {
                setValue(null);
            } else if (XssHolder.isEnabled()) {
                String value = XssUtils.clean(text);
                setValue(value);
                log.trace("Request parameter value:{} cleaned up by mica-xss, current value is:{}.", text, value);
            } else {
                setValue(text);
            }
        }

    }
}
