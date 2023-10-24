package com.qrcb.common.extension.sequence.range;

import com.qrcb.common.extension.sequence.exception.SeqException;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 区间管理器 <br/>
 */

public interface SeqRangeMgr {

    /**
     * 获取指定区间名的下一个区间
     *
     * @param name 区间名
     * @return 返回区间
     * @throws SeqException 异常
     */
    SeqRange nextRange(String name) throws SeqException;

    /**
     * 初始化
     */
    void init();
}
