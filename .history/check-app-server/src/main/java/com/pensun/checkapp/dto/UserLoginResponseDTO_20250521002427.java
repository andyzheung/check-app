package com.pensun.checkapp.dto;

import lombok.Data;

/**
 * 用户登录响应DTO
 */
@Data
public class UserLoginResponseDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 角色
     */
    private String role;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 访问令牌
     */
    private String token;
} 