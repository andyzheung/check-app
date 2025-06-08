package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.*;
import com.pensun.checkapp.entity.User;

import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return token
     */
    String login(UserLoginDTO loginDTO);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    UserDTO getCurrentUser();

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    IPage<UserDTO> getUserPage(UserQueryDTO queryDTO);

    /**
     * 创建用户
     *
     * @param createDTO 用户信息
     * @return 用户ID
     */
    Long createUser(UserCreateDTO createDTO);

    /**
     * 更新用户
     *
     * @param createDTO 用户信息
     */
    void updateUser(UserCreateDTO createDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return 用户权限
     */
    UserPermissionDTO getUserPermissions(Long userId);

    /**
     * 更新用户权限
     *
     * @param updateDTO 权限信息
     */
    void updateUserPermissions(UserPermissionUpdateDTO updateDTO);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoDTO getUserInfo(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param username 用户名
     * @param department 部门
     * @param role     角色
     * @param status   状态
     * @return 分页结果
     */
    PageResult<UserDTO> getUserList(int page, int pageSize, String username, String department, String role, String status);
} 