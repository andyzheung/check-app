package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.dto.AdUserDTO;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.exception.AdAuthenticationException;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * AD域认证服务实现
 */
@Slf4j
@Service
@Profile("prod")
public class AdServiceImpl implements AdService {

    @Value("${ldap.url:ldap://192.168.1.100:389}")
    private String ldapUrl;

    @Value("${ldap.base:dc=company,dc=local}")
    private String ldapBase;

    @Value("${ldap.user-search-base:ou=users,dc=company,dc=local}")
    private String userSearchBase;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean validateUser(String username, String password) {
        try {
            // 1. 构建用户DN
            String userDn = String.format("CN=%s,%s", username, userSearchBase);

            // 2. 构建LDAP环境配置
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, userDn);
            env.put(Context.SECURITY_CREDENTIALS, password);

            // 3. 尝试连接（验证密码）
            DirContext ctx = new InitialDirContext(env);
            ctx.close();

            log.info("AD域认证成功: {}", username);
            return true;
        } catch (NamingException e) {
            log.error("AD域认证失败: {}, 原因: {}", username, e.getMessage());
            return false;
        }
    }

    @Override
    public List<AdUserDTO> searchUsers(String keyword) {
        // 模拟AD用户搜索，实际项目中需要连接真实AD
        List<AdUserDTO> mockUsers = getMockAdUsers();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return mockUsers;
        }
        
        return mockUsers.stream()
                .filter(user -> user.getDisplayName().contains(keyword) 
                             || user.getUsername().contains(keyword)
                             || user.getDepartment().contains(keyword))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<AdUserDTO> getAllUsers() {
        // 返回所有模拟AD用户
        return getMockAdUsers();
    }

    @Override
    public AdUserDTO getUserByUsername(String username) {
        return getMockAdUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Long syncUserToSystem(String adUsername, String systemRole) {
        // 查找AD用户信息
        AdUserDTO adUser = getUserByUsername(adUsername);
        if (adUser == null) {
            throw new RuntimeException("AD用户不存在: " + adUsername);
        }
        
        // 检查系统中是否已存在该用户
        User existingUser = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                        .eq("ad_username", adUsername));
        
        if (existingUser != null) {
            // 更新现有用户角色
            existingUser.setRole(systemRole);
            existingUser.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(existingUser);
            return existingUser.getId();
        } else {
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(adUser.getUsername());
            newUser.setRealName(adUser.getDisplayName());
            newUser.setEmail(adUser.getEmail());
            newUser.setPhone(adUser.getPhone());
            newUser.setRole(systemRole);
            newUser.setAdUsername(adUsername);
            newUser.setIsAdUser(true);
            newUser.setAdSyncTime(LocalDateTime.now());
            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            newUser.setStatus("active");
            
            userMapper.insert(newUser);
            return newUser.getId();
        }
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
        
        AdUserDTO user4 = new AdUserDTO();
        user4.setUsername("zhao.liu");
        user4.setDisplayName("赵六");
        user4.setDepartment("运维部");
        user4.setEmail("zhao.liu@company.com");
        user4.setPhone("13800138004");
        users.add(user4);
        
        return users;
    }
} 