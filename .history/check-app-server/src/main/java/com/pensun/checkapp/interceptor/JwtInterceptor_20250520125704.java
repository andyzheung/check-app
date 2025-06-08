package com.pensun.checkapp.interceptor;

import com.pensun.checkapp.common.exception.BusinessException;
import com.pensun.checkapp.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new BusinessException("未登录");
        }
        
        // 验证token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException("登录已过期");
        }
        
        return true;
    }
} 