package com.pensun.checkapp.interceptor;

import com.pensun.checkapp.common.ResultCode;
import com.pensun.checkapp.util.JwtTokenUtil;
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
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            response.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }

        // 2. 验证token
        if (!jwtTokenUtil.validateToken(token)) {
            response.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }

        return true;
    }
} 