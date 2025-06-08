package com.pensun.checkapp.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Map<String, Object> result = new HashMap<>();
        // 简单示例，实际应查数据库
        if ("admin".equals(username) && "123456".equals(password)) {
            result.put("token", "mock-token-123456");
            Map<String, Object> user = new HashMap<>();
            user.put("id", 1);
            user.put("name", "管理员");
            user.put("dept", "运维部");
            user.put("role", "admin");
            result.put("user", user);
        } else {
            result.put("error", "用户名或密码错误");
        }
        return result;
    }
} 