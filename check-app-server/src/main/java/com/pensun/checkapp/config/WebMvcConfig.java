package com.pensun.checkapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origin-patterns}")
    private String allowedOriginPatterns;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${cors.max-age}")
    private long maxAge;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${qrcode.output-dir}")
    private String qrcodeOutputDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("WebMvcConfig.addCorsMappings() - allowedOriginPatterns: " + allowedOriginPatterns);
        System.out.println("WebMvcConfig.addCorsMappings() - allowCredentials: " + allowCredentials);
        
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.split(","))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传文件静态资源
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
        
        // 二维码静态资源
        registry.addResourceHandler("/qrcode/**")
                .addResourceLocations("file:" + qrcodeOutputDir + "/")
                .setCachePeriod(86400); // 缓存24小时
    }
} 