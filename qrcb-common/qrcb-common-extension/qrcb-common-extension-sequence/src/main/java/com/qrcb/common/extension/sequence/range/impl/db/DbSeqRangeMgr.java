package com.qrcb.common.extension.sequence.range.impl.db;

import cn.hutool.core.util.StrUtil;
import com.qrcb.common.extension.sequence.exception.SeqException;
import com.qrcb.common.extension.sequence.range.SeqRange;
import com.qrcb.common.extension.sequence.range.SeqRangeMgr;

import javax.sql.DataSource;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description DB 区间管理器 <br/>
 */

public class DbSeqRangeMgr implements SeqRangeMgr {

    /**
     * 区间步长
     */
    private int step = 1000;

    /**
     * 区间起始位置，真实从stepStart+1开始
     */
    private long stepStart = 0;

    /**
     * 获取区间失败重试次数
     */
    private int retryTimes = 100;

    /**
     * DB来源
     */
    private DataSource dataSource;

    /**
     * 数据库名，默认qrcb
     */
    String dbSchema = "qrcb";

    /**
     * 表名，默认range
     */
    private String tableName = "range";

    @Override
    public SeqRange nextRange(String name) throws SeqException {
        if (isEmpty(name)) {
            throw new SecurityException("[DbSeqRangeMgr-nextRange] name is empty.");
        }

        Long oldValue;
        Long newValue;

        for (int i = 0; i < getRetryTimes(); i++) {
            oldValue = BaseDbHelper.selectRange(getDataSource(), getRealTableName(), name, getStepStart());

            if (null == oldValue) {
                // 区间不存在，重试
                continue;
            }

            newValue = oldValue + getStep();

            if (BaseDbHelper.updateRange(getDataSource(), getRealTableName(), newValue, oldValue, name)) {
                return new SeqRange(oldValue + 1, newValue);
            }
            // else 失败重试
        }

        throw new SeqException("Retried too many times, retryTimes = " + getRetryTimes());
    }

    @Override
    public void init() {
        checkParam();
        BaseDbHelper.createTable(getDataSource(), getRealTableName());
    }

    private boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    private String getRealTableName() {
        return getDbSchema() + StrUtil.DOT + getTableName();
    }

    private void checkParam() {
        if (step <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-checkParam] step must greater than 0.");
        }
        if (stepStart < 0) {
            throw new SecurityException("[DbSeqRangeMgr-setStepStart] stepStart < 0.");
        }
        if (retryTimes <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-setRetryTimes] retryTimes must greater than 0.");
        }
        if (null == dataSource) {
            throw new SecurityException("[DbSeqRangeMgr-setDataSource] dataSource is null.");
        }
        if (isEmpty(dbSchema)) {
            throw new SecurityException("[DbSeqRangeMgr-setDbSchema] dbSchema is empty.");
        }
        if (isEmpty(tableName)) {
            throw new SecurityException("[DbSeqRangeMgr-setTableName] tableName is empty.");
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getStepStart() {
        return stepStart;
    }

    public void setStepStart(long stepStart) {
        this.stepStart = stepStart;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
