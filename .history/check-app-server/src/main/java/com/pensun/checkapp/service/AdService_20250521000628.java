package com.pensun.checkapp.service;

/**
 * AD域认证服务接口
 */
public interface AdService {
    
    /**
     * 验证AD域账号密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 验证结果
     */
    boolean authenticate(String username, String password);
} 