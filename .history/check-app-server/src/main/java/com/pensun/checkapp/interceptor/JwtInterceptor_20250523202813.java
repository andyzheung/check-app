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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
        if (path.endsWith("/api/v1/login") ||
            path.endsWith("/api/v1/register") ||
            path.endsWith("/api/v1/captcha") ||
            path.endsWith("/api/v1/users/login")) {
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
                String username = jwtTokenUtil.getUsernameFromToken(token);
                // 通过Spring上下文获取UserService
                ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
                com.pensun.checkapp.service.UserService userService = ctx.getBean(com.pensun.checkapp.service.UserService.class);
                com.pensun.checkapp.entity.User user = userService.getByUsername(username);
                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, new java.util.ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
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