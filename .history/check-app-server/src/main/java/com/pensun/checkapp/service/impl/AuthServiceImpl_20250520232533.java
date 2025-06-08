package com.pensun.checkapp.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.StrUtil;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.LoginDTO;
import com.pensun.checkapp.dto.RegisterDTO;
import com.pensun.checkapp.service.AuthService;
import com.pensun.checkapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result login(LoginDTO loginDTO) {
        // 验证验证码
        String captchaKey = "captcha:" + loginDTO.getCaptchaKey();
        String captcha = redisTemplate.opsForValue().get(captchaKey);
        if (StrUtil.isBlank(captcha)) {
            return Result.error("验证码已过期");
        }
        if (!captcha.equalsIgnoreCase(loginDTO.getCaptcha())) {
            return Result.error("验证码错误");
        }
        redisTemplate.delete(captchaKey);

        // TODO: 实现用户登录逻辑
        // 1. 验证用户名密码
        // 2. 生成token
        // 3. 返回用户信息

        return Result.success();
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        // 验证两次密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return Result.error("两次密码不一致");
        }

        // TODO: 实现用户注册逻辑
        // 1. 检查用户名是否已存在
        // 2. 加密密码
        // 3. 保存用户信息

        return Result.success();
    }

    @Override
    public Result generateCaptcha() {
        // 生成验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 4, 100);
        String captchaKey = StrUtil.uuid();
        
        // 保存验证码到Redis
        redisTemplate.opsForValue().set(
            "captcha:" + captchaKey,
            captcha.getCode(),
            5,
            TimeUnit.MINUTES
        );

        // 返回验证码图片和key
        Map<String, String> data = new HashMap<>();
        data.put("captchaKey", captchaKey);
        data.put("captchaImage", captcha.getImageBase64());

        return Result.success(data);
    }

    @Override
    public Result logout() {
        // TODO: 实现登出逻辑
        // 1. 清除token
        // 2. 清除用户信息
        return Result.success();
    }
} 