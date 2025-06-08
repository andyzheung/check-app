package com.pensun.checkapp.controller;

import com.pensun.checkapp.dto.UserLoginDTO;
import com.pensun.checkapp.dto.UserLoginResponseDTO;
import com.pensun.checkapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[AuthController] 收到登录请求: " + loginDTO.getUsername());
        UserLoginResponseDTO resp = userService.login(loginDTO);
        System.out.println("[AuthController] 登录响应: " + resp);
        return resp;
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // 可扩展：记录日志、token黑名单等
        return Result.success();
    }
} 