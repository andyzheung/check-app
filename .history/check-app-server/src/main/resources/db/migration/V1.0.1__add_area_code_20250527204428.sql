-- 添加区域编码相关字段
ALTER TABLE t_area
ADD COLUMN area_code VARCHAR(8) NULL COMMENT '区域编码' AFTER id,
ADD COLUMN qr_code_url VARCHAR(255) COMMENT '二维码图片URL' AFTER area_code,
ADD COLUMN area_name VARCHAR(50) NULL COMMENT '区域名称' AFTER qr_code_url,
ADD COLUMN area_type CHAR(1) NULL COMMENT '区域类型：A-机房,B-办公区,C-设备区' AFTER area_name;

-- 更新已有数据
UPDATE t_area SET 
  area_code = CONCAT('AREA', 'A', LPAD(id, 3, '0')),
  area_name = CONCAT('区域', id),
  area_type = 'A'
WHERE area_code IS NULL;

-- 添加非空约束和唯一索引
ALTER TABLE t_area
MODIFY COLUMN area_code VARCHAR(8) NOT NULL COMMENT '区域编码',
MODIFY COLUMN area_name VARCHAR(50) NOT NULL COMMENT '区域名称',
MODIFY COLUMN area_type CHAR(1) NOT NULL COMMENT '区域类型：A-机房,B-办公区,C-设备区',
ADD UNIQUE INDEX uk_area_code (area_code);

-- 创建区域类型表
CREATE TABLE t_area_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_code CHAR(1) NOT NULL COMMENT '类型编码',
    type_name VARCHAR(50) NOT NULL COMMENT '类型名称',
    description VARCHAR(200) COMMENT '类型描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_type_code (type_code)
) COMMENT '区域类型表';

-- 插入初始区域类型数据
INSERT INTO t_area_type (type_code, type_name, description) VALUES
('A', '机房', '各类机房区域'),
('B', '办公区', '办公室、会议室等区域'),
('C', '设备区', '设备安装、维护区域');

-- 插入测试用的区域数据
INSERT INTO t_inspection_area (area_code, area_name, area_type, qr_code_url, status) VALUES
('AREAA001', '机房A', 'A', NULL, 'active'),
('AREAA002', '机房B', 'A', NULL, 'active'),
('AREAA003', '机房C', 'A', NULL, 'active'); 