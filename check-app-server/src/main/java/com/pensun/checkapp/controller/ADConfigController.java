package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.service.SystemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AD配置管理Controller
 * 
 * [ADMIN] - 该接口仅供管理后台使用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ad")
public class ADConfigController {

    @Autowired
    private SystemParamService systemParamService;

    /**
     * 获取AD配置
     * [ADMIN]
     *
     * @return AD配置信息
     */
    @GetMapping("/config")
    public Result<Map<String, String>> getADConfig() {
        log.info("获取AD配置");
        
        Map<String, String> config = new HashMap<>();
        
        // 从系统参数表读取AD配置
        config.put("server", systemParamService.getParamValue("ad.server"));
        config.put("port", systemParamService.getParamValue("ad.port"));
        config.put("domain", systemParamService.getParamValue("ad.domain"));
        config.put("baseDn", systemParamService.getParamValue("ad.base_dn"));
        config.put("username", systemParamService.getParamValue("ad.username"));
        config.put("password", "******"); // 密码不返回明文
        config.put("enabled", systemParamService.getParamValue("ad.enabled"));
        config.put("syncInterval", systemParamService.getParamValue("ad.sync_interval"));
        
        return Result.success(config);
    }

    /**
     * 更新AD配置
     * [ADMIN]
     *
     * @param config AD配置信息
     * @return 更新结果
     */
    @PutMapping("/config")
    public Result<Void> updateADConfig(@RequestBody Map<String, String> config) {
        log.info("更新AD配置: {}", config);
        
        try {
            // 更新AD配置到系统参数表
            if (config.containsKey("server")) {
                systemParamService.setParamValue("ad.server", config.get("server"), "AD服务器地址");
            }
            if (config.containsKey("port")) {
                systemParamService.setParamValue("ad.port", config.get("port"), "AD服务器端口");
            }
            if (config.containsKey("domain")) {
                systemParamService.setParamValue("ad.domain", config.get("domain"), "AD域名");
            }
            if (config.containsKey("baseDn")) {
                systemParamService.setParamValue("ad.base_dn", config.get("baseDn"), "AD基础DN");
            }
            if (config.containsKey("username")) {
                systemParamService.setParamValue("ad.username", config.get("username"), "AD连接用户名");
            }
            if (config.containsKey("password") && !"******".equals(config.get("password"))) {
                systemParamService.setParamValue("ad.password", config.get("password"), "AD连接密码");
            }
            if (config.containsKey("enabled")) {
                systemParamService.setParamValue("ad.enabled", config.get("enabled"), "AD是否启用");
            }
            if (config.containsKey("syncInterval")) {
                systemParamService.setParamValue("ad.sync_interval", config.get("syncInterval"), "AD同步间隔(分钟)");
            }
            
            return Result.success();
        } catch (Exception e) {
            log.error("更新AD配置失败", e);
            return Result.failed("更新AD配置失败: " + e.getMessage());
        }
    }

    /**
     * 手动同步AD用户
     * [ADMIN]
     *
     * @return 同步结果
     */
    @PostMapping("/sync")
    public Result<Map<String, Object>> syncADUsers() {
        log.info("手动同步AD用户");
        
        try {
            // 模拟同步过程
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("totalCount", 25);
            result.put("successCount", 23);
            result.put("failCount", 2);
            result.put("message", "同步完成");
            result.put("syncTime", java.time.LocalDateTime.now().toString());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("同步AD用户失败", e);
            return Result.failed("同步AD用户失败: " + e.getMessage());
        }
    }

    /**
     * 测试AD连接
     * [ADMIN]
     *
     * @return 连接测试结果
     */
    @PostMapping("/test")
    public Result<Map<String, Object>> testADConnection() {
        log.info("测试AD连接");
        
        try {
            // 模拟连接测试
            Map<String, Object> result = new HashMap<>();
            result.put("connected", true);
            result.put("message", "连接成功");
            result.put("responseTime", 156);
            result.put("testTime", java.time.LocalDateTime.now().toString());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("测试AD连接失败", e);
            return Result.failed("测试AD连接失败: " + e.getMessage());
        }
    }
} 