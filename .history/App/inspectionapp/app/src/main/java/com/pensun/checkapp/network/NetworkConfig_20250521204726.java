package com.pensun.checkapp.network;

public class NetworkConfig {
    // 开发环境配置
    public static final String DEV_BASE_URL = "http://10.0.2.2:8080/";  // 模拟器访问本机
    public static final String DEV_BASE_URL_REAL = "http://192.168.1.100:8080/";  // 真机访问本机，需要替换为实际IP
    
    // 生产环境配置
    public static final String PROD_BASE_URL = "https://api.your-domain.com/";  // 生产环境地址
    
    // 当前环境配置
    public static final String BASE_URL = DEV_BASE_URL;  // 默认使用开发环境模拟器配置
} 