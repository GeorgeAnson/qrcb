package com.qrcb.common.extension.sequence.sequence;

import com.qrcb.common.extension.sequence.range.BizName;
import com.qrcb.common.extension.sequence.range.SeqRangeMgr;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号区间生成器接口 <br/>
 */

public interface RangeSequence extends Sequence {

    /**
     * 设置区间管理器
     *
     * @param seqRangeMgr 区间管理器
     */
    void setSeqRangeMgr(SeqRangeMgr seqRangeMgr);

    /**
     * 设置获取序列号名称
     *
     * @param name 名称
     */
    void setName(BizName name);

    /**
     * 设置号码格式
     *
     * @param format 号码格式
     */
    void setFormat(String format);
}