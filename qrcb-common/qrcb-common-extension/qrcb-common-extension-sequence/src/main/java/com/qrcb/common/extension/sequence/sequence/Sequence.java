package com.qrcb.common.extension.sequence.sequence;

import com.qrcb.common.extension.sequence.exception.SeqException;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号生成器接口 <br/>
 */

public interface Sequence {

    /**
     * 生成下一个序列号
     *
     * @return 序列号
     * @throws SeqException 序列号异常
     */
    long nextValue() throws SeqException;

    /**
     * 下一个生成序号（带格式）
     *
     * @return
     * @throws SeqException
     */
    String nextBizNo() throws SeqException;

}
