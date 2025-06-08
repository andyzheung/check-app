package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.service.UserPermissionService;
import org.springframework.stereotype.Service;

/**
 * 用户权限服务实现
 */
@Service
public class UserPermissionServiceImpl implements UserPermissionService {
    
    @Override
    public void initDefaultPermissions(Long userId) {
        // TODO: 实现用户默认权限初始化逻辑
        // 这里先留空，实际项目中需要根据业务需求设置默认权限
    }
} 