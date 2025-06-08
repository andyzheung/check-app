package com.pensun.checkapp.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户权限DTO
 */
@Data
public class UserPermissionDTO {
    
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
     * 权限代码列表
     */
    private List<String> permissions;
} 