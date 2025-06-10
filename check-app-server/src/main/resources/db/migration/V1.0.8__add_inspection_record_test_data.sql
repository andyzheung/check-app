-- 巡检记录测试数据 V1.0.8
-- 为巡检记录表插入测试数据

-- 确保管理员用户存在
SET @admin_id = (SELECT id FROM t_user WHERE username = 'admin');
INSERT INTO t_user (username, password, real_name, department_id, role, status)
SELECT 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'Administrator', 1, 'admin', 'active'
WHERE NOT EXISTS (SELECT 1 FROM t_user WHERE username = 'admin');
SET @admin_id = IFNULL(@admin_id, LAST_INSERT_ID());

-- 确保区域存在
SET @area1_id = (SELECT id FROM t_area WHERE code = 'AREA101' LIMIT 1);
SET @area2_id = (SELECT id FROM t_area WHERE code = 'AREA102' LIMIT 1);
SET @area3_id = (SELECT id FROM t_area WHERE code = 'AREA103' LIMIT 1);

-- 检查巡检记录是否已存在
SET @record_count = (SELECT COUNT(*) FROM t_inspection_record);
SET @should_insert_records = IF(@record_count > 0, 0, 1);

-- 只有当没有巡检记录时才插入测试数据
INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, remark, route_path, create_time, update_time, deleted)
SELECT * FROM (
    SELECT 
    'R202506010001', @area1_id, @admin_id, '2025-06-01 09:00:00', '2025-06-01 10:30:00', 'normal', 
    '例行巡检，一切正常', 
    JSON_ARRAY(
        JSON_OBJECT('timestamp', '2025-06-01 09:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
        JSON_OBJECT('timestamp', '2025-06-01 09:15:00', 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'Equipment Area 1'),
        JSON_OBJECT('timestamp', '2025-06-01 09:45:00', 'latitude', 30.66000, 'longitude', 104.06550, 'location', 'Equipment Area 2'),
        JSON_OBJECT('timestamp', '2025-06-01 10:30:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
    ), 
    NOW(), NOW(), 0
) AS temp
WHERE @should_insert_records = 1 AND NOT EXISTS (SELECT 1 FROM t_inspection_record WHERE record_no = 'R202506010001');

INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, remark, route_path, create_time, update_time, deleted)
SELECT * FROM (
    SELECT 
    'R202506010002', @area2_id, @admin_id, '2025-06-01 14:00:00', '2025-06-01 15:30:00', 'abnormal', 
    '发现电源间温度异常', 
    JSON_ARRAY(
        JSON_OBJECT('timestamp', '2025-06-01 14:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
        JSON_OBJECT('timestamp', '2025-06-01 14:20:00', 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'Power Room'),
        JSON_OBJECT('timestamp', '2025-06-01 15:00:00', 'latitude', 30.66000, 'longitude', 104.06550, 'location', 'Cooling System'),
        JSON_OBJECT('timestamp', '2025-06-01 15:30:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
    ), 
    NOW(), NOW(), 0
) AS temp
WHERE @should_insert_records = 1 AND NOT EXISTS (SELECT 1 FROM t_inspection_record WHERE record_no = 'R202506010002');

INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, remark, route_path, create_time, update_time, deleted)
SELECT * FROM (
    SELECT 
    'R202506020001', @area3_id, @admin_id, '2025-06-02 09:00:00', '2025-06-02 10:15:00', 'normal', 
    '网络设备工作正常', 
    JSON_ARRAY(
        JSON_OBJECT('timestamp', '2025-06-02 09:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
        JSON_OBJECT('timestamp', '2025-06-02 09:30:00', 'latitude', 30.65995, 'longitude', 104.06548, 'location', 'Network Room'),
        JSON_OBJECT('timestamp', '2025-06-02 10:15:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
    ), 
    NOW(), NOW(), 0
) AS temp
WHERE @should_insert_records = 1 AND NOT EXISTS (SELECT 1 FROM t_inspection_record WHERE record_no = 'R202506020001');

INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, remark, route_path, create_time, update_time, deleted)
SELECT * FROM (
    SELECT 
    'R202506030001', @area1_id, @admin_id, '2025-06-03 09:00:00', '2025-06-03 11:00:00', 'normal', 
    '安全设施完好', 
    JSON_ARRAY(
        JSON_OBJECT('timestamp', '2025-06-03 09:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
        JSON_OBJECT('timestamp', '2025-06-03 09:30:00', 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'Security Area 1'),
        JSON_OBJECT('timestamp', '2025-06-03 10:15:00', 'latitude', 30.66000, 'longitude', 104.06550, 'location', 'Security Area 2'),
        JSON_OBJECT('timestamp', '2025-06-03 11:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
    ), 
    NOW(), NOW(), 0
) AS temp
WHERE @should_insert_records = 1 AND NOT EXISTS (SELECT 1 FROM t_inspection_record WHERE record_no = 'R202506030001');

INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, remark, route_path, create_time, update_time, deleted)
SELECT * FROM (
    SELECT 
    'R202506040001', @area2_id, @admin_id, '2025-06-04 13:00:00', '2025-06-04 14:30:00', 'abnormal', 
    '发现UPS电池状态异常', 
    JSON_ARRAY(
        JSON_OBJECT('timestamp', '2025-06-04 13:00:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
        JSON_OBJECT('timestamp', '2025-06-04 13:30:00', 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'UPS Room'),
        JSON_OBJECT('timestamp', '2025-06-04 14:30:00', 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
    ), 
    NOW(), NOW(), 0
) AS temp
WHERE @should_insert_records = 1 AND NOT EXISTS (SELECT 1 FROM t_inspection_record WHERE record_no = 'R202506040001');

-- 获取记录ID，如果不存在则为NULL
SET @record1_id = (SELECT id FROM t_inspection_record WHERE record_no = 'R202506010001' LIMIT 1);
SET @record2_id = (SELECT id FROM t_inspection_record WHERE record_no = 'R202506010002' LIMIT 1);
SET @record3_id = (SELECT id FROM t_inspection_record WHERE record_no = 'R202506020001' LIMIT 1);
SET @record4_id = (SELECT id FROM t_inspection_record WHERE record_no = 'R202506030001' LIMIT 1);
SET @record5_id = (SELECT id FROM t_inspection_record WHERE record_no = 'R202506040001' LIMIT 1);

-- 为记录1添加巡检项，如果记录存在且巡检项不存在
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record1_id, '温度检查', 'environment', 'normal', '温度正常，20-22°C', NOW(), NOW()
WHERE @record1_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record1_id AND name = '温度检查' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record1_id, '湿度检查', 'environment', 'normal', '湿度正常，40-50%', NOW(), NOW()
WHERE @record1_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record1_id AND name = '湿度检查' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record1_id, '噪音检查', 'environment', 'normal', '噪音在可接受范围内', NOW(), NOW()
WHERE @record1_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record1_id AND name = '噪音检查' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record1_id, '门禁系统', 'security', 'normal', '门禁系统工作正常', NOW(), NOW()
WHERE @record1_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record1_id AND name = '门禁系统' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record1_id, '监控系统', 'security', 'normal', '监控系统工作正常', NOW(), NOW()
WHERE @record1_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record1_id AND name = '监控系统' AND type = 'security'
);

-- 为记录2添加巡检项（含异常项），如果记录存在且巡检项不存在
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record2_id, '温度检查', 'environment', 'abnormal', '温度异常，28°C，超出正常范围', NOW(), NOW()
WHERE @record2_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record2_id AND name = '温度检查' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record2_id, '湿度检查', 'environment', 'normal', '湿度正常，45%', NOW(), NOW()
WHERE @record2_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record2_id AND name = '湿度检查' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record2_id, '空调系统', 'environment', 'abnormal', '空调制冷效果不佳，需要维修', NOW(), NOW()
WHERE @record2_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record2_id AND name = '空调系统' AND type = 'environment'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record2_id, '门禁系统', 'security', 'normal', '门禁系统工作正常', NOW(), NOW()
WHERE @record2_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record2_id AND name = '门禁系统' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record2_id, '消防设备', 'security', 'normal', '消防设备完好', NOW(), NOW()
WHERE @record2_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record2_id AND name = '消防设备' AND type = 'security'
);

-- 为记录3添加巡检项，如果记录存在且巡检项不存在
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record3_id, '网络设备', 'device', 'normal', '路由器和交换机工作正常', NOW(), NOW()
WHERE @record3_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record3_id AND name = '网络设备' AND type = 'device'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record3_id, '网络连接', 'device', 'normal', '网络连接稳定，延迟正常', NOW(), NOW()
WHERE @record3_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record3_id AND name = '网络连接' AND type = 'device'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record3_id, '网络安全', 'security', 'normal', '防火墙和安全设备工作正常', NOW(), NOW()
WHERE @record3_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record3_id AND name = '网络安全' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record3_id, '温度检查', 'environment', 'normal', '设备温度正常', NOW(), NOW()
WHERE @record3_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record3_id AND name = '温度检查' AND type = 'environment'
);

-- 为记录4添加巡检项，如果记录存在且巡检项不存在
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record4_id, '消防设备', 'security', 'normal', '灭火器有效期内，喷淋系统正常', NOW(), NOW()
WHERE @record4_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record4_id AND name = '消防设备' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record4_id, '应急照明', 'security', 'normal', '应急照明工作正常', NOW(), NOW()
WHERE @record4_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record4_id AND name = '应急照明' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record4_id, '疏散通道', 'security', 'normal', '疏散通道畅通', NOW(), NOW()
WHERE @record4_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record4_id AND name = '疏散通道' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record4_id, '安全标识', 'security', 'normal', '安全标识清晰可见', NOW(), NOW()
WHERE @record4_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record4_id AND name = '安全标识' AND type = 'security'
);

-- 为记录5添加巡检项（含异常项），如果记录存在且巡检项不存在
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record5_id, 'UPS设备', 'device', 'abnormal', 'UPS电池显示需要更换', NOW(), NOW()
WHERE @record5_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record5_id AND name = 'UPS设备' AND type = 'device'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record5_id, '配电设备', 'device', 'normal', '配电柜工作正常', NOW(), NOW()
WHERE @record5_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record5_id AND name = '配电设备' AND type = 'device'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record5_id, '接地系统', 'security', 'normal', '接地系统正常', NOW(), NOW()
WHERE @record5_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record5_id AND name = '接地系统' AND type = 'security'
);

INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time)
SELECT @record5_id, '温度检查', 'environment', 'normal', '温度在正常范围内', NOW(), NOW()
WHERE @record5_id IS NOT NULL AND NOT EXISTS (
    SELECT 1 FROM t_inspection_item WHERE record_id = @record5_id AND name = '温度检查' AND type = 'environment'
); 