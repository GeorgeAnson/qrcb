package com.qrcb.common.extension.sequence.builder;

import com.qrcb.common.extension.sequence.sequence.Sequence;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号生成器构建者 <br/>
 */

public interface SeqBuilder {

    /**
     * 构建一个序列号生成器
     *
     * @return 序列号生成器
     */
    Sequence build();

}
