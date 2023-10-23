package com.qrcb.common.core.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.security.exception.QrcbAuth2Exception;
import lombok.SneakyThrows;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description <br/>
 */

public class QrcbAuth2ExceptionSerializer extends StdSerializer<QrcbAuth2Exception> {

    public QrcbAuth2ExceptionSerializer() {
        super(QrcbAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(QrcbAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", R.Constants.FAIL);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorCode());
        gen.writeEndObject();
    }

}
