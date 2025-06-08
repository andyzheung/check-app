package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户权限Mapper接口
 */
@Mapper
public interface UserPermissionMapper extends BaseMapper<UserPermission> {
    
    /**
     * 根据用户ID查询权限代码列表
     *
     * @param userId 用户ID
     * @return 权限代码列表
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID删除权限
     *
     * @param userId 用户ID
     */
    void deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 批量插入权限
     *
     * @param permissions 权限列表
     */
    void batchInsert(@Param("permissions") List<UserPermission> permissions);
} 