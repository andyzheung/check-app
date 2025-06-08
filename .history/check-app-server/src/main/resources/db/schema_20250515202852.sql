-- 创建数据库
CREATE DATABASE IF NOT EXISTS check_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE check_app;

-- 部门表
CREATE TABLE IF NOT EXISTS t_department (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT COMMENT '父部门ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    department_id BIGINT COMMENT '部门ID',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活，locked-已锁定',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像',
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

-- 区域巡检项模板表
CREATE TABLE IF NOT EXISTS t_area_check_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    area_id BIGINT NOT NULL COMMENT '区域ID',
    name VARCHAR(100) NOT NULL COMMENT '巡检项名称',
    type VARCHAR(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项',
    sort INT DEFAULT 0 COMMENT '排序',
    required TINYINT DEFAULT 1 COMMENT '是否必填：0-否，1-是',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域巡检项模板表';

-- 巡检任务表
CREATE TABLE IF NOT EXISTS t_inspection_task (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    area_id BIGINT NOT NULL COMMENT '巡检区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    plan_time DATETIME COMMENT '计划巡检时间',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '任务状态：pending-待执行，processing-执行中，completed-已完成，canceled-已取消',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检任务表';

-- 巡检记录表
CREATE TABLE IF NOT EXISTS t_inspection_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    record_no VARCHAR(50) NOT NULL COMMENT '记录编号',
    task_id BIGINT COMMENT '巡检任务ID',
    area_id BIGINT NOT NULL COMMENT '巡检区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    start_time DATETIME NOT NULL COMMENT '巡检开始时间',
    end_time DATETIME COMMENT '巡检结束时间',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '巡检状态：normal-正常，abnormal-异常',
    remark VARCHAR(500) COMMENT '备注',
    route_path TEXT COMMENT '巡检路径（JSON格式存储）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_record_no (record_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检记录表';

-- 巡检项表
CREATE TABLE IF NOT EXISTS t_inspection_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '巡检项ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    name VARCHAR(100) NOT NULL COMMENT '巡检项名称',
    type VARCHAR(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项',
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
    issue_no VARCHAR(50) NOT NULL COMMENT '问题编号',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    item_id BIGINT COMMENT '巡检项ID',
    description VARCHAR(500) NOT NULL COMMENT '问题描述',
    type VARCHAR(50) COMMENT '问题类型：environment-环境问题，security-安全问题，device-设备问题，other-其他',
    status VARCHAR(20) NOT NULL DEFAULT 'open' COMMENT '问题状态：open-未处理，processing-处理中，closed-已关闭',
    recorder_id BIGINT NOT NULL COMMENT '记录人ID',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME COMMENT '处理时间',
    handle_result VARCHAR(500) COMMENT '处理结果',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_issue_no (issue_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题表';

-- 问题处理记录表
CREATE TABLE IF NOT EXISTS t_issue_process (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    issue_id BIGINT NOT NULL COMMENT '问题ID',
    action VARCHAR(20) NOT NULL COMMENT '操作类型：create-创建，process-处理，close-关闭',
    processor_id BIGINT NOT NULL COMMENT '处理人ID',
    process_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
    content VARCHAR(500) NOT NULL COMMENT '处理内容',
    images TEXT COMMENT '图片（JSON数组存储多张图片URL）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题处理记录表';

-- 初始化部门
INSERT INTO t_department (name, code, parent_id, sort, status)
VALUES 
('IT部门', 'it', NULL, 1, 1),
('运维部门', 'ops', NULL, 2, 1),
('安防部门', 'security', NULL, 3, 1);

-- 初始化管理员用户
INSERT INTO t_user (username, password, real_name, department_id, role, status)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 1, 'admin', 'active');

-- 初始化普通用户
INSERT INTO t_user (username, password, real_name, department_id, role, status)
VALUES 
('zhangsan', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '张三', 2, 'user', 'active'),
('lisi', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '李四', 3, 'user', 'active'),
('wangwu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '王五', 2, 'user', 'active'),
('zhaoliu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '赵六', 3, 'admin', 'active');

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

-- 初始化区域
INSERT INTO t_area (code, name, description, status)
VALUES 
('AREA001', '机房A', '主机房', 'active'),
('AREA002', '机房B', '备用机房', 'active'),
('AREA003', '机房C', '测试机房', 'active'),
('AREA004', '配电室', '主配电室', 'active'),
('AREA005', '监控室', '安防监控室', 'active');

-- 初始化区域巡检项模板
INSERT INTO t_area_check_item (area_id, name, type, sort, required)
VALUES 
(1, '温度', 'environment', 1, 1),
(1, '湿度', 'environment', 2, 1),
(1, '噪音', 'environment', 3, 1),
(1, '门禁', 'security', 4, 1),
(1, '监控', 'security', 5, 1),
(1, '消防设备', 'security', 6, 1),
(2, '温度', 'environment', 1, 1),
(2, '湿度', 'environment', 2, 1),
(2, '噪音', 'environment', 3, 1),
(2, '门禁', 'security', 4, 1),
(2, '监控', 'security', 5, 1),
(2, '消防设备', 'security', 6, 1),
(3, '温度', 'environment', 1, 1),
(3, '湿度', 'environment', 2, 1),
(3, '噪音', 'environment', 3, 1),
(3, '门禁', 'security', 4, 1),
(3, '监控', 'security', 5, 1),
(3, '消防设备', 'security', 6, 1); 