-- 确保admin用户存在
INSERT INTO t_user (username, password, real_name, department_id, role, status)
SELECT 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 1, 'admin', 'active'
WHERE NOT EXISTS (
    SELECT 1 FROM t_user WHERE username = 'admin'
);

-- 获取admin用户ID
SET @admin_id = (SELECT id FROM t_user WHERE username = 'admin');

-- 插入admin的巡检区域
INSERT INTO t_area (area_code, qr_code_url, area_name, area_type, code, name, description, address, type, status, create_time, update_time, deleted)
VALUES 
('AREA101', NULL, '主机房A区', 'A', 'AREA101', '主机房A区', '主要服务器机房', '数据中心1层', 'server_room', 'active', NOW(), NOW(), 0),
('AREA102', NULL, '配电室B区', 'B', 'AREA102', '配电室B区', '主要配电室', '数据中心2层', 'power_room', 'active', NOW(), NOW(), 0),
('AREA103', NULL, '网络机房C区', 'C', 'AREA103', '网络机房C区', '核心网络设备机房', '数据中心3层', 'network_room', 'active', NOW(), NOW(), 0);

SELECT id FROM t_area WHERE code = 'AREA101'

SELECT * FROM t_area;

-- 获取区域ID
SET @area1_id = (SELECT id FROM t_area WHERE code = 'AREA101');
SET @area2_id = (SELECT id FROM t_area WHERE code = 'AREA102');
SET @area3_id = (SELECT id FROM t_area WHERE code = 'AREA103');

-- 插入admin的今日待完成任务
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
('主机房A区日常巡检', @area1_id, @admin_id, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0),
('配电室B区日常巡检', @area2_id, @admin_id, DATE_ADD(NOW(), INTERVAL 2 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0),
('网络机房C区日常巡检', @area3_id, @admin_id, DATE_ADD(NOW(), INTERVAL 3 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0);

-- 插入admin的已完成任务
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
('主机房A区早班巡检', @area1_id, @admin_id, DATE_ADD(NOW(), INTERVAL -2 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0),
('配电室B区早班巡检', @area2_id, @admin_id, DATE_ADD(NOW(), INTERVAL -3 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0),
('网络机房C区早班巡检', @area3_id, @admin_id, DATE_ADD(NOW(), INTERVAL -4 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0); 


## 20250531插入admin的今日待完成任务
-- 设置变量
SET @today_start = CONCAT(CURDATE(), ' 00:00:00');
SET @today_end = CONCAT(CURDATE(), ' 23:59:59');

-- 获取admin用户ID
SET @admin_id = (SELECT id FROM t_user WHERE username = 'admin');

-- 获取区域ID
SET @area1_id = (SELECT id FROM t_area WHERE code = 'AREA101' LIMIT 1);
SET @area2_id = (SELECT id FROM t_area WHERE code = 'AREA102' LIMIT 1);
SET @area3_id = (SELECT id FROM t_area WHERE code = 'AREA103' LIMIT 1);

-- 先检查变量是否正确设置
SELECT 
    @admin_id as '管理员ID',
    @area1_id as '区域1ID',
    @area2_id as '区域2ID',
    @area3_id as '区域3ID',
    @today_start as '今天开始时间',
    @today_end as '今天结束时间';

-- 插入今日待完成任务
INSERT INTO t_inspection_task 
    (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
    -- 待完成任务
    ('主机房A区日常巡检', @area1_id, @admin_id, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0),
    ('配电室B区日常巡检', @area2_id, @admin_id, DATE_ADD(NOW(), INTERVAL 2 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0),
    ('网络机房C区日常巡检', @area3_id, @admin_id, DATE_ADD(NOW(), INTERVAL 3 HOUR), 'PENDING', @admin_id, NOW(), NOW(), 0),
    
    -- 已完成任务
    ('主机房A区早班巡检', @area1_id, @admin_id, DATE_ADD(NOW(), INTERVAL -2 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0),
    ('配电室B区早班巡检', @area2_id, @admin_id, DATE_ADD(NOW(), INTERVAL -3 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0),
    ('网络机房C区早班巡检', @area3_id, @admin_id, DATE_ADD(NOW(), INTERVAL -4 HOUR), 'COMPLETED', @admin_id, NOW(), NOW(), 0);

-- 验证插入结果
SELECT 
    id,
    task_name,
    area_id,
    inspector_id,
    plan_time,
    status,
    create_time
FROM t_inspection_task 
WHERE 
    inspector_id = @admin_id 
    AND plan_time BETWEEN @today_start AND @today_end
ORDER BY plan_time;