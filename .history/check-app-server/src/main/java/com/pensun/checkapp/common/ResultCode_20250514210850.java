package com.pensun.checkapp.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "暂未登录或token已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    
    // 用户相关错误
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_ACCOUNT_LOCKED(1003, "账号已被锁定"),
    USER_ACCOUNT_EXPIRED(1004, "账号已过期"),
    
    // 业务相关错误
    AREA_NOT_EXIST(2001, "巡检区域不存在"),
    RECORD_NOT_EXIST(2002, "巡检记录不存在"),
    ISSUE_NOT_EXIST(2003, "问题不存在"),
    TASK_NOT_EXIST(2004, "任务不存在"),
    FORM_NOT_EXIST(2005, "表单不存在"),
    
    // 系统相关错误
    SYSTEM_ERROR(9001, "系统错误"),
    DATA_ERROR(9002, "数据异常");

    private final Integer code;
    private final String message;
} 