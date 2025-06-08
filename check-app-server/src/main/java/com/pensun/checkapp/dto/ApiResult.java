package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API结果DTO
 */
@Data
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    protected ApiResult() {
    }

    protected ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(500, message, null);
    }

    /**
     * 失败
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> failed() {
        return new ApiResult<>(500, "操作失败", null);
    }

    public static <T> ApiResult<T> validateFailed() {
        return new ApiResult<>(404, "参数检验失败", null);
    }

    public static <T> ApiResult<T> unauthorized() {
        return new ApiResult<>(401, "暂未登录或token已经过期", null);
    }

    public static <T> ApiResult<T> forbidden() {
        return new ApiResult<>(403, "没有相关权限", null);
    }
} 