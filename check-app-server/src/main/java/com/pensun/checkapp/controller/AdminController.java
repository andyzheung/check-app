package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AdUserDTO;
import com.pensun.checkapp.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台控制器
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final AdService adService;
    
    /**
     * 搜索AD用户
     */
    @GetMapping("/ad-users/search")
    public Result<List<AdUserDTO>> searchAdUsers(@RequestParam(required = false) String keyword) {
        log.info("管理员搜索AD用户: {}", keyword);
        List<AdUserDTO> users = adService.searchUsers(keyword);
        return Result.success(users);
    }
    
    /**
     * 获取所有AD用户
     */
    @GetMapping("/ad-users")
    public Result<List<AdUserDTO>> getAllAdUsers() {
        log.info("管理员获取所有AD用户");
        List<AdUserDTO> users = adService.getAllUsers();
        return Result.success(users);
    }
    
    /**
     * 为AD用户分配系统角色
     */
    @PostMapping("/ad-users/{adUsername}/assign-role")
    public Result<Void> assignRoleToAdUser(
            @PathVariable String adUsername,
            @RequestParam String role) {
        log.info("管理员为AD用户分配角色: {} -> {}", adUsername, role);
        
        try {
            Long userId = adService.syncUserToSystem(adUsername, role);
            log.info("成功为AD用户分配角色: {} (系统用户ID: {})", adUsername, userId);
            return Result.success();
        } catch (Exception e) {
            log.error("分配角色失败: {}", e.getMessage());
            return Result.error("分配角色失败: " + e.getMessage());
        }
    }
} 