package com.qrcb.common.extension.sequence.builder;

import com.qrcb.common.extension.sequence.range.BizName;
import com.qrcb.common.extension.sequence.range.impl.db.DbSeqRangeMgr;
import com.qrcb.common.extension.sequence.sequence.Sequence;
import com.qrcb.common.extension.sequence.sequence.impl.DefaultRangeSequence;

import javax.sql.DataSource;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 基于DB取步长，序列号生成器构建者 <br/>
 */

public class DbSeqBuilder implements SeqBuilder {

    /**
     * 数据库数据源[必选]
     */
    private DataSource dataSource;

    /**
     * 业务名称[必选]
     */
    private BizName bizName;

    /**
     * 存放序列号步长的表的存放位置
     */
    private String dbSchema="qrcb";

    /**
     * 存放序列号步长的表[可选：默认：sequence]
     */
    private String tableName = "sequence";

    /**
     * 并发是数据使用了乐观策略，这个是失败重试的次数[可选：默认：100]
     */
    private int retryTimes = 100;

    /**
     * 获取range步长[可选：默认：1000]
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

    public static DbSeqBuilder create() {
        DbSeqBuilder builder = new DbSeqBuilder();
        return builder;
    }

    @Override
    public Sequence build() {
        // 利用DB获取区间管理器
        DbSeqRangeMgr dbSeqRangeMgr = new DbSeqRangeMgr();
        dbSeqRangeMgr.setDataSource(this.dataSource);
        dbSeqRangeMgr.setDbSchema(this.dbSchema);
        dbSeqRangeMgr.setTableName(this.tableName);
        dbSeqRangeMgr.setRetryTimes(this.retryTimes);
        dbSeqRangeMgr.setStep(this.step);
        dbSeqRangeMgr.setStepStart(stepStart);
        dbSeqRangeMgr.init();
        // 构建序列号生成器
        DefaultRangeSequence sequence = new DefaultRangeSequence();
        sequence.setName(this.bizName);
        sequence.setFormat(this.format);
        sequence.setSeqRangeMgr(dbSeqRangeMgr);
        return sequence;
    }

    public DbSeqBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public DbSeqBuilder dbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
        return this;
    }

    public DbSeqBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public DbSeqBuilder retryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public DbSeqBuilder step(int step) {
        this.step = step;
        return this;
    }

    public DbSeqBuilder bizName(BizName bizName) {
        this.bizName = bizName;
        return this;
    }

    public DbSeqBuilder stepStart(long stepStart) {
        this.stepStart = stepStart;
        return this;
    }

    public DbSeqBuilder format(String  format) {
        this.format = format;
        return this;
    }
}