package com.qrcb.common.extension.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.qrcb.common.extension.xss.util.XssUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description jackson xss 处理 <br/>
 */

@Slf4j
public class JacksonXssClean extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // XSS filter
        String text = p.getValueAsString();
        if (text == null) {
            return null;
        }
        else if (XssHolder.isEnabled()) {
            String value = XssUtils.clean(text);
            log.trace("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
            return value;
        }
        else {
            return text;
        }
    }

}
