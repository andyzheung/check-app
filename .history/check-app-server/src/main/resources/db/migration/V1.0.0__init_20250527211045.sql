-- 创建用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_username (username)
) COMMENT '用户表';

-- 创建巡检区域表
CREATE TABLE IF NOT EXISTS t_inspection_area (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    area_code VARCHAR(8) NOT NULL COMMENT '区域编码',
    qr_code_url VARCHAR(255) COMMENT '二维码图片URL',
    area_name VARCHAR(50) NOT NULL COMMENT '区域名称',
    area_type CHAR(1) NOT NULL COMMENT '区域类型：A-机房,B-办公区,C-设备区',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活',
    description VARCHAR(200) COMMENT '区域描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_area_code (area_code)
) COMMENT '巡检区域表';

-- 创建区域类型表
CREATE TABLE IF NOT EXISTS t_area_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_code CHAR(1) NOT NULL COMMENT '类型编码',
    type_name VARCHAR(50) NOT NULL COMMENT '类型名称',
    description VARCHAR(200) COMMENT '类型描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_type_code (type_code)
) COMMENT '区域类型表';

-- 创建巡检记录表
CREATE TABLE IF NOT EXISTS t_inspection_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(20) NOT NULL COMMENT '记录编号',
    area_id BIGINT NOT NULL COMMENT '区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    start_time DATETIME NOT NULL COMMENT '巡检开始时间',
    end_time DATETIME COMMENT '巡检结束时间',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待巡检，processing-巡检中，completed-已完成',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_record_no (record_no)
) COMMENT '巡检记录表';

-- 创建巡检项表
CREATE TABLE IF NOT EXISTS t_inspection_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    name VARCHAR(100) NOT NULL COMMENT '巡检项名称',
    type VARCHAR(20) NOT NULL COMMENT '类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal-正常，abnormal-异常',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '巡检项表';

-- 插入初始区域类型数据
INSERT INTO t_area_type (type_code, type_name, description) VALUES
('A', '机房', '各类机房区域'),
('B', '办公区', '办公室、会议室等区域'),
('C', '设备区', '设备安装、维护区域');

-- 插入测试用的区域数据
INSERT INTO t_inspection_area (area_code, area_name, area_type, status) VALUES
('AREAA001', '机房A', 'A', 'active'),
('AREAA002', '机房B', 'A', 'active'),
('AREAA003', '机房C', 'A', 'active');

-- 插入默认管理员账号
INSERT INTO t_user (username, password, real_name, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHsM8', '系统管理员', 'active'); 