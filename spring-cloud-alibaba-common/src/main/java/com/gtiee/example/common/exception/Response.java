package com.gtiee.example.common.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求统一响应实体类
 *
 * @param <T> 返回结果类型
 * @author wentao.wu
 */
@Data
@ApiModel(value = "请求响应实体类")
public class Response<T> {
    @ApiModelProperty(value = "请求成功响应编码")
    private String code;
    @ApiModelProperty(value = "请求成功响应消息")
    private String msg;
    @ApiModelProperty(value = "请求失败响应编码")
    private String errorCode;
    @ApiModelProperty(value = "请求失败响应消息")
    private String errorMsg;
    @ApiModelProperty(value = "返回结果")
    private T result;

    public static <T> Response<T> createOk(String msg, T result) {
        Response<T> response = new Response<>();
        response.setCode("1");
        response.setMsg(msg);
        response.setResult(result);
        return response;
    }

    public static <T> Response<T> createError(String errorMsg, T result) {
        Response<T> response = new Response<>();
        response.setErrorCode("1");
        response.setErrorCode(errorMsg);
        response.setResult(result);
        return response;
    }

    public boolean isOk() {
        if ("1".equals(this.code)) {
            return true;
        }
        return false;
    }
}
