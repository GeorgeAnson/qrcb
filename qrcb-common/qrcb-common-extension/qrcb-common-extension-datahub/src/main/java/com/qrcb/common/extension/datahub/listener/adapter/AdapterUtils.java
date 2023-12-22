package com.qrcb.common.extension.datahub.listener.adapter;

import com.aliyun.datahub.client.model.RecordEntry;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@UtilityClass
public class AdapterUtils {

    @Nullable
    public Object buildRecordEntryMetadataFromArray(Object... data) {
        for (Object object : data) {
            RecordEntry metadata = buildConsumerRecordMetadata(object);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }

    @Nullable
    public RecordEntry buildConsumerRecordMetadata(Object data) {
        if (!(data instanceof RecordEntry)) {
            return null;
        }
        return (RecordEntry) data;
    }
}
