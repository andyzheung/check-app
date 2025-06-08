package com.pensun.checkapp.exception;

/**
 * AD域认证异常
 */
public class AdAuthenticationException extends RuntimeException {

    public AdAuthenticationException(String message) {
        super(message);
    }

    public AdAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 