package com.qrcb.common.core.assemble.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Anson
 * @Create 2023-07-26
 * @Description api返回工具类<br />
 */

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回标记：成功=0，失败=1")
    private int code;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回信息")
    private String msg;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回数据")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, Constants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, Constants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, Constants.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, Constants.FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, Constants.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, Constants.FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, Constants.FAIL, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public interface Constants {
        /**
         * 成功标记
         */
        Integer SUCCESS = 0;

        /**
         * 失败标记
         */
        Integer FAIL = 1;
    }
}