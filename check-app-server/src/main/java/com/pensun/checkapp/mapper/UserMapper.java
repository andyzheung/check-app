package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 根据AD用户名查询用户
     *
     * @param adUsername AD用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM t_user WHERE ad_username = #{adUsername} AND deleted = 0")
    User selectByAdUsername(@Param("adUsername") String adUsername);

    /**
     * 查询所有AD用户
     *
     * @return AD用户列表
     */
    @Select("SELECT * FROM t_user WHERE is_ad_user = 1 AND deleted = 0")
    List<User> selectAdUsers();

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param username 用户名
     * @param departmentId 部门ID
     * @param role 角色
     * @param status 状态
     * @return 用户列表
     */
    IPage<User> selectUserPage(Page<User> page,
                             @Param("username") String username,
                             @Param("departmentId") Long departmentId,
                             @Param("role") String role,
                             @Param("status") String status);
} 