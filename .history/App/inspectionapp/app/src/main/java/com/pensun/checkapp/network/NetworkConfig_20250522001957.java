package com.pensun.checkapp.network;

public class NetworkConfig {
    // 模拟器访问本机Spring Boot服务使用10.0.2.2
    public static final String BASE_URL = "http://10.0.2.2:8080";
    
    // 真机调试时使用本机实际IP地址
    // public static final String BASE_URL = "http://192.168.1.100:8080";  // 替换为你的实际IP
} 