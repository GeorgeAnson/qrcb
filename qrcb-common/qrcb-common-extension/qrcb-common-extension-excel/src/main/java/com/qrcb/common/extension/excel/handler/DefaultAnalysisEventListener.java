package com.qrcb.common.extension.excel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.qrcb.common.extension.excel.annotation.ExcelLine;
import com.qrcb.common.extension.excel.kit.ErrorMessage;
import com.qrcb.common.extension.excel.kit.Validators;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description 默认 excel 读取解析监听器 <br/>
 */

@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {
    private final List<Object> list = new ArrayList<>();

    private final List<ErrorMessage> errorMessageList = new ArrayList<>();

    private Long lineNum = 1L;

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        lineNum++;

        Set<ConstraintViolation<Object>> violations = Validators.validate(o);
        if (!violations.isEmpty()) {
            Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            errorMessageList.add(new ErrorMessage(lineNum, messageSet));
        } else {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Long.class) {
                    try {
                        field.setAccessible(true);
                        field.set(o, lineNum);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(o);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("Excel read analysed");
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return errorMessageList;
    }

}
