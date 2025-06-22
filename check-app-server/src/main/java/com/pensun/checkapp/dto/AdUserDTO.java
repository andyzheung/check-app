package com.pensun.checkapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AD用户DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUserDTO {
    
    /**
     * AD用户名
     */
    private String username;
    
    /**
     * 显示名称
     */
    private String displayName;
    
    /**
     * 部门
     */
    private String department;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 是否已分配系统权限
     */
    private Boolean hasSystemRole;
    
    /**
     * 系统角色
     */
    private String systemRole;
    
    /**
     * 系统用户ID（如果已分配）
     */
    private Long systemUserId;
} 