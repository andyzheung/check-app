package com.pensun.checkapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * 启动类
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class CheckAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckAppApplication.class, args);
    }
} 