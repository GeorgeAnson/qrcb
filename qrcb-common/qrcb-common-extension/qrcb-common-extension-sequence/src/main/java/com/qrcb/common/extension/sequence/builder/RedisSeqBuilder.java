package com.qrcb.common.extension.sequence.builder;

import com.qrcb.common.extension.sequence.range.BizName;
import com.qrcb.common.extension.sequence.range.impl.redis.RedisSeqRangeMgr;
import com.qrcb.common.extension.sequence.sequence.Sequence;
import com.qrcb.common.extension.sequence.sequence.impl.DefaultRangeSequence;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 基于 redis 取步长，序列号生成器构建者 <br/>
 */

public class RedisSeqBuilder implements SeqBuilder {

    /**
     * 连接redis的IP[必选]
     */
    private String ip;

    /**
     * 连接redis的port[必选]
     */
    private int port;

    /**
     * 业务名称[必选]
     */
    private BizName bizName;

    /**
     * 认证权限，看redis是否配置了需要密码auth[可选]
     */
    private String auth;

    /**
     * 获取range步长[可选，默认：1000]
     */
    private int step = 1000;

    /**
     * 序列号分配起始值[可选：默认：0]
     */
    private long stepStart = 0;

    /**
     * 号码格式，默认：%05d
     */
    private String format = "%05d";

    public static RedisSeqBuilder create() {
        RedisSeqBuilder builder = new RedisSeqBuilder();
        return builder;
    }

    @Override
    public Sequence build() {
        // 利用Redis获取区间管理器
        RedisSeqRangeMgr redisSeqRangeMgr = new RedisSeqRangeMgr();
        redisSeqRangeMgr.setIp(this.ip);
        redisSeqRangeMgr.setPort(this.port);
        redisSeqRangeMgr.setAuth(this.auth);
        redisSeqRangeMgr.setStep(this.step);
        redisSeqRangeMgr.setStepStart(stepStart);
        redisSeqRangeMgr.init();
        // 构建序列号生成器
        DefaultRangeSequence sequence = new DefaultRangeSequence();
        sequence.setName(this.bizName);
        sequence.setSeqRangeMgr(redisSeqRangeMgr);
        sequence.setFormat(this.format);
        return sequence;
    }

    public RedisSeqBuilder ip(String ip) {
        this.ip = ip;
        return this;
    }

    public RedisSeqBuilder port(int port) {
        this.port = port;
        return this;
    }

    public RedisSeqBuilder auth(String auth) {
        this.auth = auth;
        return this;
    }

    public RedisSeqBuilder step(int step) {
        this.step = step;
        return this;
    }

    public RedisSeqBuilder bizName(BizName bizName) {
        this.bizName = bizName;
        return this;
    }

    public RedisSeqBuilder stepStart(long stepStart) {
        this.stepStart = stepStart;
        return this;
    }

    public RedisSeqBuilder format(String  format) {
        this.format = format;
        return this;
    }
}
