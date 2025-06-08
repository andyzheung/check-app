package com.pensun.checkapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        System.out.println("===> [System.out] /api/v1/test/ping 被调用");
        return "pong";
    }
} 