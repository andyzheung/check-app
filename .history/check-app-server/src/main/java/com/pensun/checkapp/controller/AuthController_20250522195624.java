package com.pensun.checkapp.controller;

import com.pensun.checkapp.dto.UserLoginDTO;
import com.pensun.checkapp.dto.UserLoginResponseDTO;
import com.pensun.checkapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
} 