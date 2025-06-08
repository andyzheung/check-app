package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.ResultCode;
import com.pensun.checkapp.dto.*;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.service.UserService;
import com.pensun.checkapp.util.JwtTokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(UserLoginDTO loginDTO) {
        // 1. 调用AD域（LDAP）校验账号密码
        boolean adSuccess = adService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
        if (!adSuccess) {
            throw new RuntimeException("AD域账号或密码错误");
        }

        // 2. 本地用户表查找
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            // 3. 不存在则自动创建普通用户
            user = new User();
            user.setUsername(loginDTO.getUsername());
            user.setRole("user");
            user.setStatus("active");
            save(user);
            // 4. 权限表赋予普通权限
            userPermissionService.initDefaultPermissions(user.getId());
        }

        // 5. 生成token
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }

    @Override
    public UserDTO getCurrentUser() {
        // TODO: 从SecurityContext中获取当前用户
        return null;
    }

    @Override
    public IPage<UserDTO> getUserPage(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .like(StringUtils.hasText(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                .eq(StringUtils.hasText(queryDTO.getRole()), User::getRole, queryDTO.getRole())
                .eq(StringUtils.hasText(queryDTO.getStatus()), User::getStatus, queryDTO.getStatus());

        return page(page, wrapper).convert(user -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateDTO createDTO) {
        // 1. 检查用户名是否存在
        if (getByUsername(createDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建用户
        User user = new User();
        BeanUtils.copyProperties(createDTO, user);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserCreateDTO createDTO) {
        // 1. 检查用户是否存在
        User user = getById(createDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 更新用户
        BeanUtils.copyProperties(createDTO, user);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 1. 检查用户是否存在
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 逻辑删除
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    public UserPermissionDTO getUserPermissions(Long userId) {
        // TODO: 实现获取用户权限
        return null;
    }

    @Override
    public void updateUserPermissions(UserPermissionUpdateDTO updateDTO) {
        // TODO: 实现更新用户权限
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        // TODO: 实现获取用户信息
        return null;
    }

    @Override
    public PageResult<UserDTO> getUserList(int page, int pageSize, String username, String department, String role, String status) {
        // TODO: 实现分页查询用户列表
        return null;
    }
} 