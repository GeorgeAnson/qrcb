package com.qrcb.common.extension.sequence.sequence.impl;

import com.qrcb.common.extension.sequence.exception.SeqException;
import com.qrcb.common.extension.sequence.range.BizName;
import com.qrcb.common.extension.sequence.range.SeqRange;
import com.qrcb.common.extension.sequence.range.SeqRangeMgr;
import com.qrcb.common.extension.sequence.sequence.RangeSequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 序列号区间生成器接口默认实现 <br/>
 * 根据 biz name 自增
 */

public class DefaultRangeSequence  implements RangeSequence {

    /**
     * 获取区间是加一把独占锁防止资源冲突
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 序列号区间管理器
     */
    private SeqRangeMgr seqRangeMgr;

    /**
     * 当前序列号区间
     */
    private volatile SeqRange currentRange;

    private static Map<String, SeqRange> seqRangeMap = new ConcurrentHashMap<>(8);

    /**
     * 需要获取区间的业务名称
     */
    private BizName bizName;

    /**
     * 号码格式
     */
    private String format;

    @Override
    public long nextValue() throws SeqException {
        String name = bizName.create();

        currentRange = seqRangeMap.get(name);
        // 当前区间不存在，重新获取一个区间
        if (null == currentRange) {
            lock.lock();
            try {
                if (null == currentRange) {
                    currentRange = seqRangeMgr.nextRange(name);
                    seqRangeMap.put(name, currentRange);
                }
            }
            finally {
                lock.unlock();
            }
        }

        // 当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
        long value = currentRange.getAndIncrement();
        if (value == -1) {
            lock.lock();
            try {
                for (;;) {
                    if (currentRange.isOver()) {
                        currentRange = seqRangeMgr.nextRange(name);
                        seqRangeMap.put(name, currentRange);
                    }

                    value = currentRange.getAndIncrement();
                    if (value == -1) {
                        continue;
                    }

                    break;
                }
            }
            finally {
                lock.unlock();
            }
        }

        if (value < 0) {
            throw new SeqException("Sequence value overflow, value = " + value);
        }

        return value;
    }

    /**
     * 下一个生成序号（带格式）
     * @return String
     * @throws SeqException
     */
    @Override
    public String nextBizNo() throws SeqException {
        return String.format(format, nextValue());
    }

    @Override
    public void setSeqRangeMgr(SeqRangeMgr seqRangeMgr) {
        this.seqRangeMgr = seqRangeMgr;
    }

    @Override
    public void setName(BizName name) {
        this.bizName = name;
    }

    @Override
    public void setFormat(String format) {
        this.format=format;
    }
}