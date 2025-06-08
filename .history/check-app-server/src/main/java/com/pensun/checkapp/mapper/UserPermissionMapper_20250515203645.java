package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户权限Mapper接口
 */
@Mapper
public interface UserPermissionMapper extends BaseMapper<UserPermission> {

    /**
     * 根据用户ID删除权限
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE t_user_permission SET deleted = 1 WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
} 