-- 创建巡检区域表
CREATE TABLE t_inspection_area (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-启用,inactive-停用',
    description VARCHAR(200) COMMENT '区域描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否,1-是'
) COMMENT '巡检区域表';

-- 创建巡检记录表
CREATE TABLE t_inspection_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    area_id INT NOT NULL COMMENT '区域ID',
    inspector_id INT NOT NULL COMMENT '巡检人ID',
    inspection_time DATETIME NOT NULL COMMENT '巡检时间',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal-正常,abnormal-异常',
    remark TEXT COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否,1-是',
    FOREIGN KEY (area_id) REFERENCES t_inspection_area(id)
) COMMENT '巡检记录表';

-- 创建巡检项目表
CREATE TABLE t_inspection_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    area_id INT NOT NULL COMMENT '区域ID',
    item_name VARCHAR(50) NOT NULL COMMENT '项目名称',
    item_type VARCHAR(20) NOT NULL COMMENT '项目类型：environment-环境,security-安全',
    required TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否必填：0-否,1-是',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否,1-是',
    FOREIGN KEY (area_id) REFERENCES t_inspection_area(id)
) COMMENT '巡检项目表';

-- 创建巡检记录明细表
CREATE TABLE t_inspection_record_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL COMMENT '记录ID',
    item_id INT NOT NULL COMMENT '项目ID',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal-正常,abnormal-异常',
    remark TEXT COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否,1-是',
    FOREIGN KEY (record_id) REFERENCES t_inspection_record(id),
    FOREIGN KEY (item_id) REFERENCES t_inspection_item(id)
) COMMENT '巡检记录明细表'; 