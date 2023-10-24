package com.qrcb.common.extension.sequence.range.impl.name;

import com.qrcb.common.extension.sequence.range.BizName;
import lombok.AllArgsConstructor;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 根据传入返回 bizName <br/>
 */

@AllArgsConstructor
public class DefaultBizName implements BizName {

    private String bizName;

    /**
     * 生成空间名称
     */
    @Override
    public String create() {
        return bizName;
    }

}
