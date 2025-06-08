package com.pensun.checkapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        System.out.println("===> [System.out] 收到登录请求: " + body);
        String username = body.get("username");
        String password = body.get("password");
        logger.info("收到登录请求: username={}, password={}", username, password);

        Map<String, Object> result = new HashMap<>();
        if ("admin".equals(username) && "123456".equals(password)) {
            logger.info("登录成功: {}", username);
            System.out.println("===> [System.out] 登录成功: " + username);
            result.put("token", "mock-token-123456");
            Map<String, Object> user = new HashMap<>();
            user.put("id", 1);
            user.put("name", "管理员");
            user.put("dept", "运维部");
            user.put("role", "admin");
            result.put("user", user);
        } else {
            logger.warn("登录失败: {}", username);
            System.out.println("===> [System.out] 登录失败: " + username);
            result.put("error", "用户名或密码错误");
        }
        return result;
    }
} 