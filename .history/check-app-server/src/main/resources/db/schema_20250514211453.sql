-- 创建数据库
CREATE DATABASE IF NOT EXISTS check_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE check_app;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    department VARCHAR(50) COMMENT '部门',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活，locked-已锁定',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户权限表
CREATE TABLE IF NOT EXISTS t_user_permission (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限代码',
    permission_name VARCHAR(100) COMMENT '权限名称',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_permission (user_id, permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限表';

-- 巡检区域表
CREATE TABLE IF NOT EXISTS t_area (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '区域ID',
    code VARCHAR(50) NOT NULL COMMENT '区域编码',
    name VARCHAR(100) NOT NULL COMMENT '区域名称',
    description VARCHAR(500) COMMENT '区域描述',
    address VARCHAR(200) COMMENT '区域地址',
    type VARCHAR(50) COMMENT '区域类型',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '区域状态：active-活跃，inactive-未激活',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检区域表';

-- 巡检记录表
CREATE TABLE IF NOT EXISTS t_inspection_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    area_id BIGINT NOT NULL COMMENT '巡检区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    inspection_time DATETIME NOT NULL COMMENT '巡检时间',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '巡检状态：normal-正常，abnormal-异常',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检记录表';

-- 巡检项表
CREATE TABLE IF NOT EXISTS t_inspection_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '巡检项ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    name VARCHAR(100) NOT NULL COMMENT '巡检项名称',
    type VARCHAR(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '巡检项状态：normal-正常，abnormal-异常',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检项表';

-- 问题表
CREATE TABLE IF NOT EXISTS t_issue (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '问题ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    item_id BIGINT NOT NULL COMMENT '巡检项ID',
    description VARCHAR(500) NOT NULL COMMENT '问题描述',
    type VARCHAR(50) COMMENT '问题类型',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '问题状态：pending-待处理，processing-处理中，completed-已完成',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME COMMENT '处理时间',
    handle_result VARCHAR(500) COMMENT '处理结果',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 初始化管理员用户
INSERT INTO t_user (username, password, real_name, role, status)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 'admin', 'active');

-- 初始化权限
INSERT INTO t_user_permission (user_id, permission_code, permission_name)
VALUES 
(1, 'dashboard', '仪表盘查看权限'),
(1, 'records_view', '巡检记录查看权限'),
(1, 'records_all', '查看所有巡检记录'),
(1, 'records_export', '巡检记录导出权限'),
(1, 'issues_view', '问题列表查看权限'),
(1, 'issues_edit', '问题处理权限'),
(1, 'user_manage', '用户管理权限'),
(1, 'system_config', '系统配置权限'); 