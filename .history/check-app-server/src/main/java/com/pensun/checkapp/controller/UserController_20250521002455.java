package com.pensun.checkapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.UserCreateDTO;
import com.pensun.checkapp.dto.UserDTO;
import com.pensun.checkapp.dto.UserLoginDTO;
import com.pensun.checkapp.dto.UserLoginResponseDTO;
import com.pensun.checkapp.dto.UserPermissionDTO;
import com.pensun.checkapp.dto.UserPermissionUpdateDTO;
import com.pensun.checkapp.dto.UserQueryDTO;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.service.AdService;
import com.pensun.checkapp.service.UserPermissionService;
import com.pensun.checkapp.service.UserService;
import com.pensun.checkapp.util.JwtTokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Resource
    private UserService userService;
    
    @Resource
    private AdService adService;
    
    @Resource
    private UserPermissionService userPermissionService;
    
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        UserLoginResponseDTO response = userService.login(loginDTO);
        return Result.success(response);
    }
    
    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUser() {
        UserDTO userDTO = userService.getCurrentUser();
        return Result.success(userDTO);
    }
    
    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    @GetMapping("/page")
    public Result<IPage<UserDTO>> getUserPage(UserQueryDTO queryDTO) {
        IPage<UserDTO> userPage = userService.getUserPage(queryDTO);
        return Result.success(userPage);
    }
    
    /**
     * 创建用户
     *
     * @param createDTO 用户信息
     * @return 用户ID
     */
    @PostMapping
    public Result<Long> createUser(@RequestBody UserCreateDTO createDTO) {
        Long userId = userService.createUser(createDTO);
        return Result.success(userId);
    }
    
    /**
     * 更新用户
     *
     * @param createDTO 用户信息
     * @return 无
     */
    @PutMapping
    public Result<Void> updateUser(@RequestBody UserCreateDTO createDTO) {
        userService.updateUser(createDTO);
        return Result.success();
    }
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 无
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return 用户权限
     */
    @GetMapping("/{userId}/permissions")
    public Result<UserPermissionDTO> getUserPermissions(@PathVariable Long userId) {
        UserPermissionDTO permissionDTO = userService.getUserPermissions(userId);
        return Result.success(permissionDTO);
    }
    
    /**
     * 更新用户权限
     *
     * @param updateDTO 权限信息
     * @return 无
     */
    @PutMapping("/permissions")
    public Result<Void> updateUserPermissions(@RequestBody UserPermissionUpdateDTO updateDTO) {
        userService.updateUserPermissions(updateDTO);
        return Result.success();
    }
} 