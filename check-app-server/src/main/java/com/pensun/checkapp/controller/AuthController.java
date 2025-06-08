package com.pensun.checkapp.controller;

import com.pensun.checkapp.dto.UserLoginDTO;
import com.pensun.checkapp.dto.UserLoginResponseDTO;
import com.pensun.checkapp.service.UserService;
import com.pensun.checkapp.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import org.springframework.data.redis.core.StringRedisTemplate;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pensun.checkapp.util.JwtTokenUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    // @Autowired
    // private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[AuthController] 收到登录请求: " + loginDTO.getUsername());
        UserLoginResponseDTO resp = userService.login(loginDTO);
        System.out.println("[AuthController] 登录响应: " + resp);
        return resp;
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            // 记录token到黑名单，过期时间与token一致
            Long expire = jwtTokenUtil.getExpireFromToken(token);
            if (expire != null && expire > 0) {
                // stringRedisTemplate.opsForValue().set("jwt:blacklist:" + token, "1", expire, java.util.concurrent.TimeUnit.SECONDS);
                log.info("用户退出，token已加入黑名单: {}，过期时间:{}秒", token, expire);
            }
        } else {
            log.warn("退出请求未携带有效token");
        }
        return Result.success();
    }
} 