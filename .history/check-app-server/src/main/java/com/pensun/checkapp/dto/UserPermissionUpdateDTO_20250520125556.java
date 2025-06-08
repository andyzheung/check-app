package com.pensun.checkapp.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户权限更新DTO
 */
@Data
public class UserPermissionUpdateDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 权限代码列表
     */
    private List<String> permissions;
} 