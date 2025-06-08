package com.pensun.checkapp.service;

/**
 * 用户权限服务接口
 */
public interface UserPermissionService {
    
    /**
     * 初始化用户默认权限
     *
     * @param userId 用户ID
     */
    void initDefaultPermissions(Long userId);
} 