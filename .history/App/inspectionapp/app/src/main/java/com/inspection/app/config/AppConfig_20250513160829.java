package com.inspection.app.config;

public class AppConfig {
    // API基础URL配置
    private static final String DEV_API_BASE_URL = "http://10.0.2.2:8080/api/v1";  // 开发环境（模拟器）
    private static final String PROD_API_BASE_URL = "https://api.yourdomain.com/api/v1";  // 生产环境
    private static final String STAGING_API_BASE_URL = "https://staging-api.yourdomain.com/api/v1";  // 预发环境

    // 当前环境
    public enum Environment {
        DEV,
        STAGING,
        PROD
    }

    private static Environment currentEnv = Environment.DEV;

    public static String getApiBaseUrl() {
        switch (currentEnv) {
            case DEV:
                return DEV_API_BASE_URL;
            case STAGING:
                return STAGING_API_BASE_URL;
            case PROD:
                return PROD_API_BASE_URL;
            default:
                return DEV_API_BASE_URL;
        }
    }

    public static void setEnvironment(Environment env) {
        currentEnv = env;
    }

    // 其他配置项
    public static final int CONNECTION_TIMEOUT = 30; // 秒
    public static final int READ_TIMEOUT = 30; // 秒
    public static final int WRITE_TIMEOUT = 30; // 秒
} 