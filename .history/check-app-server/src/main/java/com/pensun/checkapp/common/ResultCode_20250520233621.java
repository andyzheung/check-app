package com.pensun.checkapp.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 用户相关错误
    USER_NOT_EXIST(1001, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    DUPLICATE_USERNAME(1003, "用户名已存在"),
    OLD_PASSWORD_ERROR(1004, "原密码错误"),
    
    // 区域相关错误
    AREA_NOT_EXIST(2001, "区域不存在"),
    DUPLICATE_AREA_CODE(2002, "区域编码已存在"),
    
    // 巡检点相关错误
    POINT_NOT_EXIST(3001, "巡检点不存在"),
    DUPLICATE_POINT_CODE(3002, "巡检点编码已存在"),
    
    // 巡检记录相关错误
    RECORD_NOT_EXIST(4001, "巡检记录不存在"),
    RECORD_ALREADY_COMPLETED(4002, "巡检记录已完成"),
    
    // 文件相关错误
    FILE_UPLOAD_FAILED(5001, "文件上传失败"),
    FILE_DOWNLOAD_FAILED(5002, "文件下载失败"),
    FILE_NOT_EXIST(5003, "文件不存在");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 