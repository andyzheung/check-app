-- 创建巡检记录表
CREATE TABLE IF NOT EXISTS t_inspection_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    record_no VARCHAR(32) NOT NULL COMMENT '记录编号',
    area_id BIGINT NOT NULL COMMENT '区域ID',
    inspector_id BIGINT NOT NULL COMMENT '巡检人员ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal-正常，abnormal-异常',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_record_no (record_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检记录表';

-- 创建巡检项表
CREATE TABLE IF NOT EXISTS t_inspection_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '巡检项ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    name VARCHAR(100) NOT NULL COMMENT '巡检项名称',
    type VARCHAR(20) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项',
    status VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal-正常，abnormal-异常',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    KEY idx_record_id (record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检项表'; 