package com.qrcb.common.extension.sequence.builder;

import com.qrcb.common.extension.sequence.sequence.Sequence;
import com.qrcb.common.extension.sequence.sequence.impl.SnowflakeSequence;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 基于雪花算法，序列号生成器构建者 <br/>
 */

public class SnowflakeSeqBuilder implements SeqBuilder {

    /**
     * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
     */
    private long datacenterId;

    /**
     * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
     */
    private long workerId;

    /**
     * 号码格式，默认：%d
     */
    private String format = "%d";

    public static SnowflakeSeqBuilder create() {
        SnowflakeSeqBuilder builder = new SnowflakeSeqBuilder();
        return builder;
    }

    @Override
    public Sequence build() {
        SnowflakeSequence sequence = new SnowflakeSequence();
        sequence.setDatacenterId(this.datacenterId);
        sequence.setWorkerId(this.workerId);
        sequence.setFormat(this.format);
        return sequence;
    }

    public SnowflakeSeqBuilder datacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
        return this;
    }

    public SnowflakeSeqBuilder workerId(long workerId) {
        this.workerId = workerId;
        return this;
    }

    public SnowflakeSeqBuilder format(String  format) {
        this.format = format;
        return this;
    }
}
