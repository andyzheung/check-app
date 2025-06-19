-- 数据库迁移脚本 V1.0.9
-- 支持数据中心和弱电间巡检类型

-- 1. 在t_area_type表中新增数据中心和弱电间类型
INSERT IGNORE INTO t_area_type (type_code, type_name, description) VALUES 
('D', '数据中心', '数据中心机房区域'),
('E', '弱电间', '弱电间设备区域');

-- 2. 扩展t_area表，添加模块配置支持
-- 检查module_count列是否存在
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_area' AND column_name = 'module_count' AND table_schema = DATABASE();

SET @addModuleCountCol = 'ALTER TABLE t_area ADD COLUMN module_count INT DEFAULT 0 COMMENT ''模块数量（数据中心专用）''';
SET @statement = IF(@colExists = 0, @addModuleCountCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查config_json列是否存在
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_area' AND column_name = 'config_json' AND table_schema = DATABASE();

SET @addConfigJsonCol = 'ALTER TABLE t_area ADD COLUMN config_json TEXT COMMENT ''区域配置（JSON格式）''';
SET @statement = IF(@colExists = 0, @addConfigJsonCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 创建巡检项目模板表（如果不存在）
CREATE TABLE IF NOT EXISTS t_inspection_item_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '模板ID',
    area_type CHAR(1) NOT NULL COMMENT '适用区域类型：D-数据中心, E-弱电间',
    item_name VARCHAR(100) NOT NULL COMMENT '巡检项目名称',
    item_code VARCHAR(50) NOT NULL COMMENT '巡检项目编码',
    item_type VARCHAR(20) NOT NULL DEFAULT 'boolean' COMMENT '项目类型：boolean-是否, number-数值, text-文本',
    default_value VARCHAR(100) COMMENT '默认值',
    is_required BOOLEAN DEFAULT TRUE COMMENT '是否必填',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    remark VARCHAR(500) COMMENT '备注说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='巡检项目模板表';

-- 4. 创建巡检记录详情表（如果不存在）
CREATE TABLE IF NOT EXISTS t_inspection_record_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '详情ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    item_name VARCHAR(100) NOT NULL COMMENT '巡检项目名称',
    item_code VARCHAR(50) NOT NULL COMMENT '巡检项目编码',
    item_type VARCHAR(20) NOT NULL COMMENT '项目类型',
    item_value VARCHAR(500) COMMENT '巡检结果值',
    is_normal BOOLEAN COMMENT '是否正常',
    remark VARCHAR(500) COMMENT '备注说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (record_id) REFERENCES t_inspection_record(id) ON DELETE CASCADE
) COMMENT='巡检记录详情表';

-- 5. 插入数据中心巡检项目模板
INSERT IGNORE INTO t_inspection_item_template (area_type, item_name, item_code, item_type, is_required, sort_order, remark) VALUES
('D', '机房范围内是否有声光报警', 'sound_light_alarm', 'boolean', TRUE, 1, '检查机房内是否有声光报警设备正常工作'),
('D', '机房内是否有纸箱、泡沫等火载量', 'fire_load_materials', 'boolean', TRUE, 2, '检查机房内是否存在可燃物品'),
('D', '机房内是否有漏水，天花是否有漏水', 'water_leakage', 'boolean', TRUE, 3, '检查机房地面、墙壁、天花板是否有漏水现象'),
('D', '机房环境温度', 'room_temperature', 'number', TRUE, 4, '记录机房当前环境温度（摄氏度）'),
('D', '模块回风温度检查', 'module_return_temp', 'text', FALSE, 5, '检查各模块回风温度是否正常'),
('D', '模块供电状态检查', 'module_power_status', 'text', FALSE, 6, '检查各模块供电是否正常');

-- 6. 插入弱电间巡检项目模板
INSERT IGNORE INTO t_inspection_item_template (area_type, item_name, item_code, item_type, is_required, sort_order, remark) VALUES
('E', '机房地面、墙壁和天花是否漏水', 'water_leakage_check', 'boolean', TRUE, 1, '检查弱电间地面、墙壁、天花板是否有漏水'),
('E', '机房是否有垃圾纸箱等可燃物', 'combustible_materials', 'boolean', TRUE, 2, '检查弱电间内是否存在垃圾、纸箱等可燃物品'),
('E', '机房环境温度', 'room_temperature', 'number', TRUE, 3, '记录弱电间当前环境温度（摄氏度）'),
('E', '市电和UPS双路供电是否正常', 'dual_power_supply', 'boolean', TRUE, 4, '检查市电和UPS双路供电状态'),
('E', '网络机柜是否正常上锁', 'network_cabinet_lock', 'boolean', TRUE, 5, '检查网络机柜门锁是否正常'),
('E', '机房门禁是否正常', 'access_control', 'boolean', TRUE, 6, '检查弱电间门禁系统是否正常工作'),
('E', '机房设备运行是否有异响', 'equipment_noise', 'boolean', TRUE, 7, '检查设备运行是否有异常声响'),
('E', '机房照明是否正常', 'lighting_system', 'boolean', TRUE, 8, '检查弱电间照明设备是否正常');

-- 7. 插入示例区域数据
INSERT IGNORE INTO t_area (area_code, area_name, area_type, code, name, description, address, type, status, qr_code_url, module_count, config_json) VALUES
('DC001', '数据中心1', 'D', 'DC001', '数据中心1', '主数据中心机房', '1号楼B1层', 'datacenter', 'active', NULL, 4, '{"modules": [{"id": 1, "name": "计算模块1"}, {"id": 2, "name": "计算模块2"}, {"id": 3, "name": "存储模块1"}, {"id": 4, "name": "网络模块1"}]}'),
('DC002', '数据中心2', 'D', 'DC002', '数据中心2', '备用数据中心机房', '2号楼B1层', 'datacenter', 'active', NULL, 3, '{"modules": [{"id": 1, "name": "计算模块1"}, {"id": 2, "name": "存储模块1"}, {"id": 3, "name": "网络模块1"}]}'),
('DC003', '数据中心3', 'D', 'DC003', '数据中心3', '灾备数据中心机房', '3号楼B2层', 'datacenter', 'active', NULL, 6, '{"modules": [{"id": 1, "name": "计算模块1"}, {"id": 2, "name": "计算模块2"}, {"id": 3, "name": "计算模块3"}, {"id": 4, "name": "存储模块1"}, {"id": 5, "name": "存储模块2"}, {"id": 6, "name": "网络模块1"}]}'),
('WR001', '弱电间1', 'E', 'WR001', '弱电间1', '1号楼弱电间', '1号楼1层', 'weakroom', 'active', NULL, 0, '{}'),
('WR002', '弱电间2', 'E', 'WR002', '弱电间2', '2号楼弱电间', '2号楼1层', 'weakroom', 'active', NULL, 0, '{}'),
('WR003', '弱电间3', 'E', 'WR003', '弱电间3', '3号楼弱电间', '3号楼2层', 'weakroom', 'active', NULL, 0, '{}'),
('WR004', '弱电间4', 'E', 'WR004', '弱电间4', '4号楼弱电间', '4号楼地下1层', 'weakroom', 'active', NULL, 0, '{}');

-- 8. 插入示例巡检记录数据（用于测试）
INSERT IGNORE INTO t_inspection_record (area_id, inspector_id, inspector_name, inspection_date, status, total_items, normal_items, abnormal_items, remark) VALUES
(1, 1, '张三', '2025-01-20 09:00:00', 'completed', 6, 5, 1, '数据中心1巡检完成，发现一处温度异常'),
(2, 2, '李四', '2025-01-20 14:00:00', 'completed', 8, 8, 0, '弱电间1巡检完成，一切正常'),
(3, 1, '张三', '2025-01-21 10:30:00', 'completed', 6, 6, 0, '数据中心2巡检完成，设备运行正常');

-- 9. 插入示例巡检记录详情数据
INSERT IGNORE INTO t_inspection_record_detail (record_id, template_id, item_name, item_code, item_type, item_value, is_normal, remark) VALUES
-- 数据中心1的巡检记录详情
(1, 1, '机房范围内是否有声光报警', 'sound_light_alarm', 'boolean', 'true', TRUE, '声光报警设备正常'),
(1, 2, '机房内是否有纸箱、泡沫等火载量', 'fire_load_materials', 'boolean', 'false', TRUE, '无可燃物品'),
(1, 3, '机房内是否有漏水，天花是否有漏水', 'water_leakage', 'boolean', 'false', TRUE, '无漏水现象'),
(1, 4, '机房环境温度', 'room_temperature', 'number', '28', FALSE, '温度偏高，需要调节空调'),
(1, 5, '模块回风温度检查', 'module_return_temp', 'text', '模块1:25℃,模块2:26℃,模块3:27℃,模块4:24℃', TRUE, '各模块温度正常'),
(1, 6, '模块供电状态检查', 'module_power_status', 'text', '所有模块供电正常', TRUE, '供电状态良好'),

-- 弱电间1的巡检记录详情
(2, 7, '机房地面、墙壁和天花是否漏水', 'water_leakage_check', 'boolean', 'false', TRUE, '无漏水现象'),
(2, 8, '机房是否有垃圾纸箱等可燃物', 'combustible_materials', 'boolean', 'false', TRUE, '机房整洁，无可燃物'),
(2, 9, '机房环境温度', 'room_temperature', 'number', '22', TRUE, '温度适宜'),
(2, 10, '市电和UPS双路供电是否正常', 'dual_power_supply', 'boolean', 'true', TRUE, '双路供电正常'),
(2, 11, '网络机柜是否正常上锁', 'network_cabinet_lock', 'boolean', 'true', TRUE, '机柜门锁正常'),
(2, 12, '机房门禁是否正常', 'access_control', 'boolean', 'true', TRUE, '门禁系统正常'),
(2, 13, '机房设备运行是否有异响', 'equipment_noise', 'boolean', 'false', TRUE, '设备运行安静'),
(2, 14, '机房照明是否正常', 'lighting_system', 'boolean', 'true', TRUE, '照明设备正常');

-- 10. 创建索引（如果不存在）
-- 检查索引是否存在的函数
SET @indexExists = 0;
SELECT COUNT(*) INTO @indexExists FROM information_schema.statistics 
WHERE table_name = 't_inspection_item_template' AND index_name = 'idx_area_type_active' AND table_schema = DATABASE();

SET @createIndex = 'CREATE INDEX idx_area_type_active ON t_inspection_item_template(area_type, is_active)';
SET @statement = IF(@indexExists = 0, @createIndex, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @indexExists = 0;
SELECT COUNT(*) INTO @indexExists FROM information_schema.statistics 
WHERE table_name = 't_inspection_record_detail' AND index_name = 'idx_record_id' AND table_schema = DATABASE();

SET @createIndex = 'CREATE INDEX idx_record_id ON t_inspection_record_detail(record_id)';
SET @statement = IF(@indexExists = 0, @createIndex, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 