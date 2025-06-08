package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 登录响应DTO
 */
@Data
@Accessors(chain = true)
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 认证令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo user;

    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;

        /**
         * 用户名
         */
        private String username;

        /**
         * 姓名
         */
        private String name;

        /**
         * 角色
         */
        private String role;
    }
} 