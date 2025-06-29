package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.dto.AdUserDTO;
import com.pensun.checkapp.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AD域认证服务的模拟实现，用于开发和测试环境
 */
@Slf4j
@Service
@Profile({"dev", "test"})
public class MockAdServiceImpl implements AdService {

    @Override
    public boolean validateUser(String username, String password) {
        log.warn("正在使用模拟AD认证，用户: {}, 任何密码都将通过。", username);
        // 在模拟实现中，我们总是返回true，方便开发和测试
        return true;
    }

    @Override
    public List<AdUserDTO> searchUsers(String keyword) {
        log.warn("正在使用模拟AD用户搜索");
        List<AdUserDTO> mockUsers = getMockAdUsers();
        if (keyword == null || keyword.trim().isEmpty()) {
            return mockUsers;
        }
        return mockUsers.stream()
                .filter(user -> user.getDisplayName().contains(keyword)
                             || user.getUsername().contains(keyword)
                             || user.getDepartment().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdUserDTO> getAllUsers() {
        log.warn("正在获取所有模拟AD用户");
        return getMockAdUsers();
    }

    @Override
    public AdUserDTO getUserByUsername(String username) {
        log.warn("正在按用户名获取模拟AD用户: {}", username);
        return getMockAdUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Long syncUserToSystem(String adUsername, String systemRole) {
        log.warn("正在模拟同步AD用户到系统，用户: {}, 角色: {}", adUsername, systemRole);
        // 在模拟实现中，我们不实际操作数据库，只返回一个模拟的用户ID
        return 1L;
    }

    /**
     * 获取模拟AD用户数据
     */
    private List<AdUserDTO> getMockAdUsers() {
        List<AdUserDTO> users = new ArrayList<>();

        AdUserDTO user1 = new AdUserDTO();
        user1.setUsername("zhang.san");
        user1.setDisplayName("张三");
        user1.setDepartment("运维部");
        user1.setEmail("zhang.san@company.com");
        user1.setPhone("13800138001");
        users.add(user1);

        AdUserDTO user2 = new AdUserDTO();
        user2.setUsername("li.si");
        user2.setDisplayName("李四");
        user2.setDepartment("网络部");
        user2.setEmail("li.si@company.com");
        user2.setPhone("13800138002");
        users.add(user2);

        AdUserDTO user3 = new AdUserDTO();
        user3.setUsername("wang.wu");
        user3.setDisplayName("王五");
        user3.setDepartment("安全部");
        user3.setEmail("wang.wu@company.com");
        user3.setPhone("13800138003");
        users.add(user3);
        
        return users;
    }
} 