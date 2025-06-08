package com.pensun.checkapp.interceptor;

import com.pensun.checkapp.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // @Autowired
    // private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        // Allow all paths ending with /api/v1/login
        if (path.endsWith("/api/v1/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        log.info("[JwtInterceptor] Request path: {}, Authorization: {}", path, header);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            log.info("[JwtInterceptor] Extracted token: {}", token);
            // 校验token黑名单
            // String black = stringRedisTemplate.opsForValue().get("jwt:blacklist:" + token);
            boolean valid = jwtTokenUtil.validateToken(token);
            log.info("[JwtInterceptor] Token validation result: {}", valid);
            if (valid) {
                // token 合法，放行
                filterChain.doFilter(request, response);
                return;
            }
        }
        // token 不合法或未携带，返回 401
        log.warn("[JwtInterceptor] Token is invalid or missing, access denied: {}", header);
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"Unauthorized or invalid token\"}");
    }
} 