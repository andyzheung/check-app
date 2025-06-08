package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.LoginRequest;
import com.pensun.checkapp.dto.LoginResponse;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.UserDTO;
import com.pensun.checkapp.dto.UserInfoDTO;
import com.pensun.checkapp.entity.User;

import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);

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

    /**
     * 添加用户
     *
     * @param userDTO 用户DTO
     * @return 是否成功
     */
    boolean addUser(UserDTO userDTO);

    /**
     * 更新用户
     *
     * @param id      用户ID
     * @param userDTO 用户DTO
     * @return 是否成功
     */
    boolean updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 更新用户权限
     *
     * @param id          用户ID
     * @param permissions 权限列表
     * @return 是否成功
     */
    boolean updateUserPermissions(Long id, List<String> permissions);
} 