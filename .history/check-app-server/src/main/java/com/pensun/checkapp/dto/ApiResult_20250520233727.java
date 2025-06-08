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

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>()
                .setCode(200)
                .setMessage("操作成功")
                .setData(data);
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
        return new ApiResult<T>()
                .setCode(200)
                .setMessage(message)
                .setData(data);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return API结果
     */
    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<T>()
                .setCode(500)
                .setMessage(message);
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
        return new ApiResult<T>()
                .setCode(code)
                .setMessage(message);
    }
} 