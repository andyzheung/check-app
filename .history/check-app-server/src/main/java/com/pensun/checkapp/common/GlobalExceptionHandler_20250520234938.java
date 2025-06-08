package com.pensun.checkapp.common;

import com.pensun.checkapp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return Result.error(500, e.getMessage());
    }
    
    /**
     * 处理其他异常
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(e.getMessage());
    }
} 