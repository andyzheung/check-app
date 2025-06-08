package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询权限代码列表
     *
     * @param userId 用户ID
     * @return 权限代码列表
     */
    @Select("SELECT permission_code FROM t_user_permission WHERE user_id = #{userId} AND deleted = 0")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
} 