-- 创建区域表
CREATE TABLE IF NOT EXISTS t_area (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '区域ID',
    code VARCHAR(50) NOT NULL COMMENT '区域编码',
    qr_code_url VARCHAR(255) COMMENT '二维码图片URL',
    name VARCHAR(100) NOT NULL COMMENT '区域名称',
    description VARCHAR(500) COMMENT '区域描述',
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
    PRIMARY KEY (id),
    KEY idx_area_id (area_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域巡检项模板表';

-- 巡检记录表
CREATE TABLE IF NOT EXISTS t_inspection_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    record_no VARCHAR(50) NOT NULL COMMENT '记录编号',
    area_id BIGINT NOT NULL COMMENT '巡检区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    start_time DATETIME NOT NULL COMMENT '巡检开始时间',
    end_time DATETIME COMMENT '巡检结束时间',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '巡检状态：normal-正常，abnormal-异常',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_record_no (record_no),
    KEY idx_area_id (area_id),
    KEY idx_inspector_id (inspector_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检记录表';

-- 巡检点表
CREATE TABLE IF NOT EXISTS t_inspection_point (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '巡检点ID',
    code VARCHAR(50) NOT NULL COMMENT '巡检点编码',
    name VARCHAR(100) NOT NULL COMMENT '巡检点名称',
    description VARCHAR(500) COMMENT '描述',
    address VARCHAR(200) COMMENT '地址',
    area_id BIGINT NOT NULL COMMENT '所属区域ID',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    KEY idx_area_id (area_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检点表';

-- 插入一些测试数据
INSERT INTO t_area (code, name, type, status, description) VALUES
('AREA001', '机房A区', 'A', 'active', '主要机房区域'),
('AREA002', '机房B区', 'A', 'active', '备用机房区域'),
('AREA003', '办公区', 'B', 'active', '主要办公区域'),
('AREA004', '设备区', 'C', 'active', '主要设备区域');

-- 插入一些巡检项模板
INSERT INTO t_area_check_item (area_id, name, type, sort, required) VALUES
(1, '温度检查', 'environment', 1, 1),
(1, '湿度检查', 'environment', 2, 1),
(1, '消防设备检查', 'security', 3, 1),
(1, '门禁系统检查', 'security', 4, 1),
(2, '温度检查', 'environment', 1, 1),
(2, '湿度检查', 'environment', 2, 1),
(2, '消防设备检查', 'security', 3, 1),
(2, '门禁系统检查', 'security', 4, 1); 