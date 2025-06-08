package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户创建/更新DTO
 */
@Data
public class UserCreateDTO {
    
    /**
     * 用户ID（更新时必填）
     */
    private Long id;
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    private String username;
    
    /**
     * 密码（创建时必填，更新时选填）
     */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    /**
     * 部门ID
     */
    private Long departmentId;
    
    /**
     * 角色：admin-管理员，user-普通用户
     */
    @NotBlank(message = "角色不能为空")
    private String role;
    
    /**
     * 状态：active-活跃，inactive-未激活，locked-已锁定
     */
    @NotBlank(message = "状态不能为空")
    private String status;
    
    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 头像
     */
    private String avatar;
} 