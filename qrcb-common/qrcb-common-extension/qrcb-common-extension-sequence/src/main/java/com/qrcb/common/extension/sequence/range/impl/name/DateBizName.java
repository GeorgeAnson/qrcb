package com.qrcb.common.extension.sequence.range.impl.name;

import cn.hutool.core.date.DateUtil;
import com.qrcb.common.extension.sequence.range.BizName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 根据时间重置 bizName <br/>
 */

@NoArgsConstructor
@AllArgsConstructor
public class DateBizName implements BizName {

    private String bizName;

    /**
     * 生成空间名称
     */
    @Override
    public String create() {
        return bizName + DateUtil.today();
    }

}