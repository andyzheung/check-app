package com.pensun.checkapp.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码：200-成功，其他值-失败
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 私有构造方法
     */
    private Result() {
    }

    /**
     * 私有构造方法
     *
     * @param code    状态码
     * @param message 状态信息
     * @param data    数据
     */
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     *
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败
     *
     * @param resultCode 结果码
     * @param <T>        数据类型
     * @return 返回结果
     */
    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败
     *
     * @param resultCode 结果码
     * @param message    消息
     * @param <T>        数据类型
     * @return 返回结果
     */
    public static <T> Result<T> failed(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    /**
     * 参数验证失败
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    /**
     * 错误响应
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 返回结果
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应带消息
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }
} 