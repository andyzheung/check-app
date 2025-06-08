package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.LoginRequest;
import com.pensun.checkapp.dto.LoginResponse;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.UserDTO;
import com.pensun.checkapp.dto.UserInfoDTO;
import com.pensun.checkapp.entity.Department;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.entity.UserPermission;
import com.pensun.checkapp.mapper.DepartmentMapper;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.mapper.UserPermissionMapper;
import com.pensun.checkapp.service.UserService;
import com.pensun.checkapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserPermissionMapper userPermissionMapper;
    private final DepartmentMapper departmentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginRequest.getUsername());
        queryWrapper.eq(User::getDeleted, 0);
        User user = userMapper.selectOne(queryWrapper);

        // 校验用户
        if (user == null) {
            throw new BadCredentialsException("用户不存在");
        }

        // 校验密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 校验状态
        if (!"active".equals(user.getStatus())) {
            throw new BadCredentialsException("用户已被禁用");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 返回登录响应
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setName(user.getRealName());
        userInfo.setRole(user.getRole());

        return new LoginResponse()
                .setToken(token)
                .setUser(userInfo);
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 查询权限
        List<String> permissions = userMapper.selectPermissionCodesByUserId(userId);

        // 返回用户信息
        return new UserInfoDTO()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getRealName())
                .setRole(user.getRole())
                .setPermissions(permissions);
    }

    @Override
    public PageResult<UserDTO> getUserList(int page, int pageSize, String username, String department, String role, String status) {
        // 查询部门
        List<Department> departments = departmentMapper.selectList(null);
        Map<Long, String> departmentMap = departments.stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDeleted, 0);
        if (StringUtils.hasText(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(department)) {
            List<Long> departmentIds = departments.stream()
                    .filter(d -> d.getName().contains(department))
                    .map(Department::getId)
                    .collect(Collectors.toList());
            if (!departmentIds.isEmpty()) {
                queryWrapper.in(User::getDepartmentId, departmentIds);
            }
        }
        if (StringUtils.hasText(role)) {
            queryWrapper.eq(User::getRole, role);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(User::getStatus, status);
        }
        queryWrapper.orderByDesc(User::getCreateTime);

        // 分页查询
        Page<User> userPage = new Page<>(page, pageSize);
        Page<User> resultPage = userMapper.selectPage(userPage, queryWrapper);

        // 转换为DTO
        List<UserDTO> userDTOList = resultPage.getRecords().stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setRealname(user.getRealName());
            userDTO.setDepartment(departmentMap.get(user.getDepartmentId()));
            return userDTO;
        }).collect(Collectors.toList());

        // 返回分页结果
        return PageResult.of(userDTOList, resultPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(UserDTO userDTO) {
        // 检查用户名是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        queryWrapper.eq(User::getDeleted, 0);
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRealName(userDTO.getRealname());
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);

        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(Long id, UserDTO userDTO) {
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否重复
        if (!user.getUsername().equals(userDTO.getUsername())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, userDTO.getUsername());
            queryWrapper.eq(User::getDeleted, 0);
            if (userMapper.selectCount(queryWrapper) > 0) {
                throw new RuntimeException("用户名已存在");
            }
        }

        // 更新用户
        BeanUtils.copyProperties(userDTO, user);
        user.setRealName(userDTO.getRealname());
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 逻辑删除用户
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return userMapper.selectPermissionCodesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPermissions(Long id, List<String> permissions) {
        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 删除原有权限
        userPermissionMapper.deleteByUserId(id);

        // 添加新权限
        if (permissions != null && !permissions.isEmpty()) {
            List<UserPermission> userPermissions = new ArrayList<>();
            for (String permission : permissions) {
                UserPermission userPermission = new UserPermission();
                userPermission.setUserId(id);
                userPermission.setPermissionCode(permission);
                userPermission.setCreateTime(LocalDateTime.now());
                userPermission.setUpdateTime(LocalDateTime.now());
                userPermission.setDeleted(0);
                userPermissions.add(userPermission);
            }
            return userPermissionMapper.insertBatchSomeColumn(userPermissions) > 0;
        }

        return true;
    }
} 