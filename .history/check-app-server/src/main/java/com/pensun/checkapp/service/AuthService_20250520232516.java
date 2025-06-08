package com.pensun.checkapp.service;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.LoginDTO;
import com.pensun.checkapp.dto.RegisterDTO;

public interface AuthService {
    Result login(LoginDTO loginDTO);
    Result register(RegisterDTO registerDTO);
    Result generateCaptcha();
    Result logout();
} 