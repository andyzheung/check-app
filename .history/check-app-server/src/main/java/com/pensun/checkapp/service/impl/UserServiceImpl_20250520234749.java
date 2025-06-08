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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Service实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(UserLoginDTO loginDTO) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                return null;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            log.warn("登录异常: {}", e.getMessage());
        }
        return token;
    }

    @Override
    public UserDTO getCurrentUser() {
        SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        return convertToDTO(user);
    }

    @Override
    public IPage<UserDTO> getUserPage(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                .like(queryDTO.getDepartmentId() != null, User::getDepartmentId, queryDTO.getDepartmentId())
                .like(StringUtils.hasText(queryDTO.getRole()), User::getRole, queryDTO.getRole())
                .like(StringUtils.hasText(queryDTO.getStatus()), User::getStatus, queryDTO.getStatus());
        return this.page(page, wrapper).convert(this::convertToDTO);
    }

    @Override
    public Long createUser(UserCreateDTO createDTO) {
        User user = new User();
        BeanUtils.copyProperties(createDTO, user);
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        this.save(user);
        return user.getId();
    }

    @Override
    public void updateUser(UserCreateDTO createDTO) {
        User user = new User();
        BeanUtils.copyProperties(createDTO, user);
        if (StringUtils.hasText(createDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        }
        this.updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        this.removeById(id);
    }

    @Override
    public UserPermissionDTO getUserPermissions(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            return null;
        }
        UserPermissionDTO permissionDTO = new UserPermissionDTO();
        permissionDTO.setUserId(userId);
        permissionDTO.setUsername(user.getUsername());
        permissionDTO.setRole(user.getRole());
        // TODO: 根据角色获取权限列表
        permissionDTO.setPermissions(new ArrayList<>());
        return permissionDTO;
    }

    @Override
    public void updateUserPermissions(UserPermissionUpdateDTO updateDTO) {
        User user = this.getById(updateDTO.getUserId());
        if (user == null) {
            return;
        }
        user.setRole(updateDTO.getRole());
        this.updateById(user);
        // TODO: 更新权限列表
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            return null;
        }
        UserInfoDTO infoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, infoDTO);
        return infoDTO;
    }

    @Override
    public PageResult<UserDTO> getUserList(int page, int pageSize, String username, String department, String role, String status) {
        Page<User> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), User::getUsername, username)
                .like(department != null, User::getDepartmentId, department)
                .like(StringUtils.hasText(role), User::getRole, role)
                .like(StringUtils.hasText(status), User::getStatus, status);
        IPage<User> userPage = this.page(pageParam, wrapper);
        List<UserDTO> userDTOList = userPage.getRecords().stream().map(this::convertToDTO).toList();
        return new PageResult<>(userDTOList, userPage.getTotal(), page, pageSize);
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
} 