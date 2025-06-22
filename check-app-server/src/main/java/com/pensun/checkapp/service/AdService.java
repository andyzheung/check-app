package com.pensun.checkapp.service;

import com.pensun.checkapp.dto.AdUserDTO;
import java.util.List;

/**
 * AD域服务接口
 */
public interface AdService {
    
    /**
     * 搜索AD用户
     * @param keyword 搜索关键词（姓名或用户名）
     * @return AD用户列表
     */
    List<AdUserDTO> searchUsers(String keyword);
    
    /**
     * 验证AD用户
     * @param username AD用户名
     * @param password 密码
     * @return 验证结果
     */
    boolean validateUser(String username, String password);
    
    /**
     * 获取所有AD用户
     * @return AD用户列表
     */
    List<AdUserDTO> getAllUsers();
    
    /**
     * 根据用户名获取AD用户信息
     * @param username AD用户名
     * @return AD用户信息
     */
    AdUserDTO getUserByUsername(String username);
    
    /**
     * 同步AD用户到系统
     * @param adUsername AD用户名
     * @param systemRole 系统角色
     * @return 系统用户ID
     */
    Long syncUserToSystem(String adUsername, String systemRole);
} 