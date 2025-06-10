-- 数据库扩展测试数据 V1.0.7
-- 为扩展的表插入测试数据

-- 确保管理员用户存在
SET @admin_id = (SELECT id FROM t_user WHERE username = 'admin');
INSERT INTO t_user (username, password, real_name, department_id, role, status)
SELECT 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'Administrator', 1, 'admin', 'active'
WHERE NOT EXISTS (SELECT 1 FROM t_user WHERE username = 'admin');
SET @admin_id = IFNULL(@admin_id, LAST_INSERT_ID());

-- 确保区域存在
SET @area1_id = (SELECT id FROM t_area WHERE code = 'AREA101' LIMIT 1);
INSERT INTO t_area (area_code, area_name, area_type, code, name, description, address, type, status, qr_code_url)
SELECT 'A101', 'Server Room A', 'A', 'AREA101', 'Server Room A', 'Main server equipment room', 'Data Center 1F', 'server', 'active', 'https://checkapp.pengxinxu.com/qrcode/area/AREA101'
WHERE NOT EXISTS (SELECT 1 FROM t_area WHERE code = 'AREA101');
SET @area1_id = IFNULL(@area1_id, LAST_INSERT_ID());

SET @area2_id = (SELECT id FROM t_area WHERE code = 'AREA102' LIMIT 1);
INSERT INTO t_area (area_code, area_name, area_type, code, name, description, address, type, status, qr_code_url)
SELECT 'B102', 'Power Room B', 'B', 'AREA102', 'Power Room B', 'Power distribution room', 'Data Center B1', 'power', 'active', 'https://checkapp.pengxinxu.com/qrcode/area/AREA102'
WHERE NOT EXISTS (SELECT 1 FROM t_area WHERE code = 'AREA102');
SET @area2_id = IFNULL(@area2_id, LAST_INSERT_ID());

SET @area3_id = (SELECT id FROM t_area WHERE code = 'AREA103' LIMIT 1);
INSERT INTO t_area (area_code, area_name, area_type, code, name, description, address, type, status, qr_code_url)
SELECT 'C103', 'Network Room C', 'C', 'AREA103', 'Network Room C', 'Network equipment room', 'Data Center 2F', 'network', 'active', 'https://checkapp.pengxinxu.com/qrcode/area/AREA103'
WHERE NOT EXISTS (SELECT 1 FROM t_area WHERE code = 'AREA103');
SET @area3_id = IFNULL(@area3_id, LAST_INSERT_ID());

-- 1. 更新区域表，添加二维码URL
UPDATE t_area SET qr_code_url = CONCAT('https://checkapp.pengxinxu.com/qrcode/area/', id) WHERE id IN (@area1_id, @area2_id, @area3_id);

-- 2. 为巡检记录添加路径数据
UPDATE t_inspection_record SET route_path = JSON_ARRAY(
    JSON_OBJECT('timestamp', DATE_FORMAT(start_time, '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance'),
    JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 5 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'Equipment Area 1'),
    JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 10 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.66000, 'longitude', 104.06550, 'location', 'Equipment Area 2'),
    JSON_OBJECT('timestamp', DATE_FORMAT(end_time, '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit')
) 
WHERE route_path IS NULL AND end_time IS NOT NULL;

-- 3. 插入系统参数测试数据
INSERT INTO t_system_param (param_key, param_value, param_desc, create_time, update_time)
VALUES
('system.name', 'Inspection System', 'System Name', NOW(), NOW()),
('system.version', 'v1.0.7', 'System Version', NOW(), NOW()),
('task.auto_assign', 'true', 'Auto Assign Tasks', NOW(), NOW()),
('task.reminder_minutes', '30', 'Task Reminder Minutes', NOW(), NOW()),
('notification.enable', 'true', 'Enable Notifications', NOW(), NOW()),
('file.upload.path', '/data/uploads', 'File Upload Path', NOW(), NOW()),
('file.allowed_types', 'jpg,jpeg,png,pdf,doc,docx', 'Allowed File Types', NOW(), NOW()),
('file.max_size', '10485760', 'Max File Size (bytes)', NOW(), NOW());

-- 4. 更新用户头像
UPDATE t_user SET avatar = CONCAT('https://checkapp.pengxinxu.com/avatars/default-', id, '.png') 
WHERE avatar IS NULL;

-- 5. 插入文件上传测试数据
INSERT INTO t_file_upload (file_name, file_path, file_size, file_type, upload_user_id, business_type, business_id, create_time)
VALUES
('manual.pdf', '/data/uploads/2025/05/manual.pdf', 2458621, 'application/pdf', @admin_id, 'SYSTEM', NULL, NOW()),
('equipment.pdf', '/data/uploads/2025/05/equipment.pdf', 1536842, 'application/pdf', @admin_id, 'SYSTEM', NULL, NOW()),
('record1.jpg', '/data/uploads/2025/05/record1.jpg', 245862, 'image/jpeg', @admin_id, 'RECORD', 1, NOW()),
('record2.jpg', '/data/uploads/2025/05/record2.jpg', 235461, 'image/jpeg', @admin_id, 'RECORD', 2, NOW()),
('device1.png', '/data/uploads/2025/05/device1.png', 156324, 'image/png', @admin_id, 'DEVICE', 1, NOW()),
('device2.png', '/data/uploads/2025/05/device2.png', 142536, 'image/png', @admin_id, 'DEVICE', 2, NOW());

-- 6. 插入统计数据缓存示例
INSERT INTO t_statistics_cache (cache_key, cache_data, expire_time, create_time, update_time)
VALUES
('inspection_count_daily', JSON_OBJECT(
    'date', CURDATE(),
    'total', 6,
    'normal', 5,
    'abnormal', 1,
    'by_area', JSON_ARRAY(
        JSON_OBJECT('area_id', @area1_id, 'area_name', 'Server Room A', 'count', 2),
        JSON_OBJECT('area_id', @area2_id, 'area_name', 'Power Room B', 'count', 2),
        JSON_OBJECT('area_id', @area3_id, 'area_name', 'Network Room C', 'count', 2)
    )
), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW(), NOW()),

('inspection_count_weekly', JSON_OBJECT(
    'start_date', DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY),
    'end_date', DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY),
    'total', 24,
    'normal', 20,
    'abnormal', 4,
    'by_day', JSON_ARRAY(
        JSON_OBJECT('date', DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), '%Y-%m-%d'), 'count', 3),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 1 DAY), '%Y-%m-%d'), 'count', 4),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 2 DAY), '%Y-%m-%d'), 'count', 3),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 3 DAY), '%Y-%m-%d'), 'count', 5),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 4 DAY), '%Y-%m-%d'), 'count', 3),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 5 DAY), '%Y-%m-%d'), 'count', 3),
        JSON_OBJECT('date', DATE_FORMAT(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 6 DAY), '%Y-%m-%d'), 'count', 3)
    )
), DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), NOW()),

('issue_statistics', JSON_OBJECT(
    'total', 10,
    'open', 3,
    'processing', 4,
    'closed', 3,
    'by_type', JSON_ARRAY(
        JSON_OBJECT('type', 'environment', 'count', 4),
        JSON_OBJECT('type', 'security', 'count', 3),
        JSON_OBJECT('type', 'device', 'count', 2),
        JSON_OBJECT('type', 'other', 'count', 1)
    )
), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW(), NOW());

-- 7. 创建一些问题记录及其处理过程
-- 创建问题记录
INSERT INTO t_issue (issue_no, record_id, item_id, description, type, status, recorder_id, handler_id, handle_time, handle_result, create_time, update_time, deleted)
VALUES
('ISS20250601001', 1, 3, 'AC temperature fluctuation', 'environment', 'closed', @admin_id, @admin_id, DATE_ADD(NOW(), INTERVAL -1 DAY), 'Replaced filter and adjusted settings', NOW(), NOW(), 0),
('ISS20250601002', 2, 7, 'Power Room B high temperature', 'environment', 'processing', @admin_id, @admin_id, NULL, NULL, NOW(), NOW(), 0);

-- 获取问题ID
SET @issue1_id = (SELECT id FROM t_issue WHERE issue_no = 'ISS20250601001');
SET @issue2_id = (SELECT id FROM t_issue WHERE issue_no = 'ISS20250601002');

-- 创建问题处理记录
INSERT INTO t_issue_process (issue_id, action, processor_id, process_time, content, images, create_time, update_time)
VALUES
(@issue1_id, 'create', @admin_id, DATE_ADD(NOW(), INTERVAL -3 DAY), 'Found AC temperature fluctuation', JSON_ARRAY('/data/uploads/2025/06/issue1_1.jpg', '/data/uploads/2025/06/issue1_2.jpg'), NOW(), NOW()),
(@issue1_id, 'process', @admin_id, DATE_ADD(NOW(), INTERVAL -2 DAY), 'Checked AC system, found clogged filter', JSON_ARRAY('/data/uploads/2025/06/issue1_3.jpg'), NOW(), NOW()),
(@issue1_id, 'close', @admin_id, DATE_ADD(NOW(), INTERVAL -1 DAY), 'Replaced AC filter, adjusted temp controls, issue resolved', JSON_ARRAY('/data/uploads/2025/06/issue1_4.jpg', '/data/uploads/2025/06/issue1_5.jpg'), NOW(), NOW()),
(@issue2_id, 'create', @admin_id, NOW(), 'Power Room B high temperature, need to check cooling', JSON_ARRAY('/data/uploads/2025/06/issue2_1.jpg'), NOW(), NOW()),
(@issue2_id, 'process', @admin_id, NOW(), 'Initial check, likely ventilation issue', JSON_ARRAY('/data/uploads/2025/06/issue2_2.jpg'), NOW(), NOW());

-- 8. 创建消息通知
INSERT INTO t_notification (user_id, title, content, type, status, create_time, update_time, deleted)
VALUES
(@admin_id, 'System Upgrade Notice', 'System will upgrade to v1.1.0 on June 15, 2025', 'system', 'unread', NOW(), NOW(), 0),
(@admin_id, 'Issue Processing Reminder', 'You have an issue to process: Power Room B high temperature', 'issue', 'unread', NOW(), NOW(), 0),
(@admin_id, 'Task Reminder', 'You have 3 inspection tasks pending', 'task', 'unread', NOW(), NOW(), 0);

-- 9. 添加特殊场景测试数据（补充部分）
-- 添加特殊类型的文件上传记录
INSERT INTO t_file_upload (file_name, file_path, file_size, file_type, upload_user_id, business_type, business_id, create_time)
VALUES
('large_blueprint.dwg', '/data/uploads/2025/06/blueprint.dwg', 8542761, 'application/acad', @admin_id, 'AREA', @area1_id, NOW()),
('empty_test.txt', '/data/uploads/2025/06/empty.txt', 0, 'text/plain', @admin_id, 'TEST', NULL, NOW()),
('max_size_file.zip', '/data/uploads/2025/06/max_size.zip', 10485759, 'application/zip', @admin_id, 'BACKUP', NULL, NOW());

-- 添加仪表盘统计数据缓存
INSERT INTO t_statistics_cache (cache_key, cache_data, expire_time, create_time, update_time)
VALUES
('dashboard_statistics', JSON_OBJECT(
    'tasks', JSON_OBJECT('total', 56, 'pending', 10, 'inProgress', 5, 'completed', 41),
    'records', JSON_OBJECT('total', 120, 'thisMonth', 24, 'lastMonth', 32),
    'issues', JSON_OBJECT('total', 25, 'open', 5, 'processing', 8, 'closed', 12),
    'areas', JSON_OBJECT('total', 12, 'active', 10, 'inactive', 2),
    'users', JSON_OBJECT('total', 35, 'active', 30, 'inactive', 5)
), DATE_ADD(NOW(), INTERVAL 1 DAY), NOW(), NOW());

-- 添加特殊系统参数
INSERT INTO t_system_param (param_key, param_value, param_desc, create_time, update_time)
VALUES
('system.maintenance_mode', 'false', 'System Maintenance Mode', NOW(), NOW()),
('system.login_attempts', '5', 'Max Login Attempts', NOW(), NOW()),
('system.password_expiry_days', '90', 'Password Expiry Days', NOW(), NOW()),
('report.export_format', 'pdf,excel,csv', 'Report Export Formats', NOW(), NOW()),
('map.default_center', '30.6598,104.0654', 'Map Default Center', NOW(), NOW());

-- 为现有问题添加特殊图片处理案例
INSERT INTO t_issue_process (issue_id, action, processor_id, process_time, content, images, create_time, update_time)
VALUES
(@issue2_id, 'document', @admin_id, NOW(), 'Added issue documentation and HD images', 
JSON_ARRAY('/data/uploads/2025/06/issue_doc_1.pdf', '/data/uploads/2025/06/issue_hd_image_1.png'), 
NOW(), NOW());

-- 更新区域3的二维码为动态格式URL
UPDATE t_area 
SET qr_code_url = CONCAT('https://checkapp.pengxinxu.com/qrcode/dynamic/', id, '?t=', UNIX_TIMESTAMP()) 
WHERE id = @area3_id;

-- 为一个巡检记录添加复杂路径数据（假设id=3的记录存在）
SET @record3_id = (SELECT id FROM t_inspection_record WHERE id = 3 LIMIT 1);
UPDATE t_inspection_record 
SET route_path = JSON_ARRAY(
  JSON_OBJECT('timestamp', DATE_FORMAT(start_time, '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Entrance', 'status', 'normal', 'speed', 0, 'accuracy', 5.2),
  JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 3 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65990, 'longitude', 104.06545, 'location', 'Equipment Area 1', 'status', 'normal', 'speed', 1.2, 'accuracy', 4.8),
  JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 5 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65995, 'longitude', 104.06548, 'location', 'Passage 1', 'status', 'checking', 'speed', 0, 'accuracy', 3.5),
  JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 10 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.66000, 'longitude', 104.06550, 'location', 'Equipment Area 2', 'status', 'alert', 'speed', 0, 'accuracy', 3.0, 'alert', 'Equipment abnormal'),
  JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 15 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.66005, 'longitude', 104.06552, 'location', 'Repair Area', 'status', 'fixing', 'speed', 0, 'accuracy', 2.5),
  JSON_OBJECT('timestamp', DATE_FORMAT(DATE_ADD(start_time, INTERVAL 20 MINUTE), '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65998, 'longitude', 104.06545, 'location', 'Return Passage', 'status', 'normal', 'speed', 1.5, 'accuracy', 4.0),
  JSON_OBJECT('timestamp', DATE_FORMAT(end_time, '%Y-%m-%d %H:%i:%s'), 'latitude', 30.65984, 'longitude', 104.06538, 'location', 'Exit', 'status', 'completed', 'speed', 0, 'accuracy', 5.0)
)
WHERE id = @record3_id; 