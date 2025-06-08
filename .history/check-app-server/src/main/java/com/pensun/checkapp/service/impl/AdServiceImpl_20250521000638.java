package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.service.AdService;
import org.springframework.stereotype.Service;

/**
 * AD域认证服务实现
 */
@Service
public class AdServiceImpl implements AdService {
    
    @Override
    public boolean authenticate(String username, String password) {
        // TODO: 实现AD域认证逻辑
        // 这里先返回true，实际项目中需要对接真实的AD域服务
        return true;
    }
} 