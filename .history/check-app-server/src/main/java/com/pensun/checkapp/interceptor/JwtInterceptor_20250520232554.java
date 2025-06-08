package com.pensun.checkapp.interceptor;

import cn.hutool.core.util.StrUtil;
import com.pensun.checkapp.common.exception.BusinessException;
import com.pensun.checkapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            response.setStatus(401);
            return false;
        }

        // 验证token
        if (!token.startsWith(jwtUtil.getTokenStartWith())) {
            response.setStatus(401);
            return false;
        }

        token = token.substring(jwtUtil.getTokenStartWith().length()).trim();
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }

        // 将用户信息存入request
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        return true;
    }
} 