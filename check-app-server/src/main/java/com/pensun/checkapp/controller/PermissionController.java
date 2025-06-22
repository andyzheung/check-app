package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    /**
     * 获取所有权限列表
     *
     * @return 权限列表
     */
    @GetMapping
    public Result<List<Map<String, Object>>> getAllPermissions() {
        log.info("获取所有权限列表");
        
        List<Map<String, Object>> permissions = new java.util.ArrayList<>();
        
        // 仪表盘权限
        permissions.add(createPermission("dashboard", "仪表盘", "dashboard", "查看仪表盘"));
        
        // 巡检记录权限
        permissions.add(createPermission("records_view", "巡检记录查看", "records", "查看巡检记录"));
        permissions.add(createPermission("records_all", "所有巡检记录", "records", "查看所有用户的巡检记录"));
        permissions.add(createPermission("records_export", "导出巡检记录", "records", "导出巡检记录"));
        
        // 问题管理权限
        permissions.add(createPermission("issues_view", "问题查看", "issues", "查看问题列表"));
        permissions.add(createPermission("issues_edit", "问题编辑", "issues", "编辑和处理问题"));
        
        // 用户管理权限
        permissions.add(createPermission("user_manage", "用户管理", "users", "管理用户账户"));
        
        // 系统配置权限
        permissions.add(createPermission("system_config", "系统配置", "system", "修改系统参数"));
        permissions.add(createPermission("area_config", "区域配置", "system", "管理区域配置"));
        permissions.add(createPermission("schedule_manage", "排班管理", "schedule", "管理巡检排班"));
        
        return Result.success(permissions);
    }
    
    private Map<String, Object> createPermission(String code, String name, String category, String description) {
        Map<String, Object> permission = new java.util.HashMap<>();
        permission.put("code", code);
        permission.put("name", name);
        permission.put("category", category);
        permission.put("description", description);
        return permission;
    }
} 