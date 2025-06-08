-- 添加二维码URL字段
ALTER TABLE t_area
ADD COLUMN qr_code_url VARCHAR(255) COMMENT '二维码图片URL' AFTER code;

-- 创建区域类型表
CREATE TABLE IF NOT EXISTS t_area_type (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    type_code CHAR(1) NOT NULL COMMENT '类型编码',
    type_name VARCHAR(50) NOT NULL COMMENT '类型名称',
    description VARCHAR(200) COMMENT '类型描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域类型表';

-- 插入初始区域类型数据
INSERT INTO t_area_type (type_code, type_name, description) VALUES
('A', '机房', '各类机房区域'),
('B', '办公区', '办公室、会议室等区域'),
('C', '设备区', '设备安装、维护区域');

-- 更新区域数据，添加区域类型
UPDATE t_area SET type = 'A' WHERE code IN ('AREA001', 'AREA002', 'AREA003');
UPDATE t_area SET type = 'B' WHERE code = 'AREA005';
UPDATE t_area SET type = 'C' WHERE code = 'AREA004'; 