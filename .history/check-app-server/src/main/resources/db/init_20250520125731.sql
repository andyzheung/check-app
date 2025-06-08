-- 创建数据库
CREATE DATABASE IF NOT EXISTS check_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE check_app;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    department_id BIGINT NOT NULL COMMENT '部门ID',
    role VARCHAR(20) NOT NULL COMMENT '角色',
    status VARCHAR(20) NOT NULL COMMENT '状态：active-正常，locked-锁定',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户权限表
CREATE TABLE IF NOT EXISTS t_user_permission (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限代码',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限表';

-- 初始化管理员账号
INSERT INTO t_user (username, password, real_name, department_id, role, status, create_time, update_time)
VALUES ('admin', '$2a$10$X/uMNuiw1UwMvqF3Uxw2sO9V7qGxK9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z', '管理员', 1, 'admin', 'active', NOW(), NOW()); 