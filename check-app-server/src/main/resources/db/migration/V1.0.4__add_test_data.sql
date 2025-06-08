-- 插入用户数据（如果还没有）
INSERT INTO t_user (username, password, real_name, department_id, role, status)
VALUES 
('inspector1', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '巡检员1', 2, 'user', 'active'),
('inspector2', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '巡检员2', 2, 'user', 'active');

-- 插入区域数据
INSERT INTO t_area (code, name, description, type, status, create_time, update_time, deleted)
VALUES 
('AREA001', '主机房A区', '主要服务器机房', 'server_room', 'active', NOW(), NOW(), 0),
('AREA002', '配电室B区', '主要配电室', 'power_room', 'active', NOW(), NOW(), 0),
('AREA003', '网络机房C区', '核心网络设备机房', 'network_room', 'active', NOW(), NOW(), 0),
('AREA004', 'UPS机房D区', 'UPS设备间', 'ups_room', 'active', NOW(), NOW(), 0),
('AREA005', '监控室E区', '安防监控室', 'monitor_room', 'active', NOW(), NOW(), 0);

-- 插入今日待完成任务
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
-- 待完成任务
('主机房A区日常巡检', 1, 1, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
('配电室B区日常巡检', 2, 1, DATE_ADD(NOW(), INTERVAL 2 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
('网络机房C区日常巡检', 3, 1, DATE_ADD(NOW(), INTERVAL 3 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
('UPS机房D区日常巡检', 4, 1, DATE_ADD(NOW(), INTERVAL 4 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
-- 已完成任务
('主机房A区早班巡检', 1, 1, DATE_ADD(NOW(), INTERVAL -2 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0),
('配电室B区早班巡检', 2, 1, DATE_ADD(NOW(), INTERVAL -3 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0),
('网络机房C区早班巡检', 3, 1, DATE_ADD(NOW(), INTERVAL -4 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0);

-- 插入区域巡检项模板
INSERT INTO t_area_check_item (area_id, name, type, sort, required)
VALUES 
-- 主机房A区巡检项
(1, '温度检查', 'environment', 1, 1),
(1, '湿度检查', 'environment', 2, 1),
(1, '空调运行状态', 'device', 3, 1),
(1, '服务器运行状态', 'device', 4, 1),
(1, '消防设备检查', 'security', 5, 1),

-- 配电室B区巡检项
(2, '供电状态', 'device', 1, 1),
(2, '温度检查', 'environment', 2, 1),
(2, '设备运行声音', 'device', 3, 1),
(2, '应急电源状态', 'device', 4, 1),
(2, '安全通道检查', 'security', 5, 1),

-- 网络机房C区巡检项
(3, '网络设备状态', 'device', 1, 1),
(3, '温度检查', 'environment', 2, 1),
(3, '湿度检查', 'environment', 3, 1),
(3, '制冷系统检查', 'device', 4, 1),
(3, '门禁系统检查', 'security', 5, 1);

-- 插入巡检记录（用于统计）
INSERT INTO t_inspection_record (record_no, task_id, area_id, inspector_id, start_time, end_time, status, create_time, update_time, deleted)
VALUES 
('REC20240514001', 5, 1, 1, DATE_ADD(NOW(), INTERVAL -2 HOUR), DATE_ADD(NOW(), INTERVAL -1 HOUR), 'normal', NOW(), NOW(), 0),
('REC20240514002', 6, 2, 1, DATE_ADD(NOW(), INTERVAL -3 HOUR), DATE_ADD(NOW(), INTERVAL -2 HOUR), 'normal', NOW(), NOW(), 0),
('REC20240514003', 7, 3, 1, DATE_ADD(NOW(), INTERVAL -4 HOUR), DATE_ADD(NOW(), INTERVAL -3 HOUR), 'normal', NOW(), NOW(), 0);

-- 插入巡检项记录
INSERT INTO t_inspection_item (record_id, name, type, status, create_time, update_time, deleted)
SELECT 
  1, 
  name, 
  type, 
  'normal', 
  NOW(), 
  NOW(), 
  0
FROM t_area_check_item 
WHERE area_id = 1;

INSERT INTO t_inspection_item (record_id, name, type, status, create_time, update_time, deleted)
SELECT 
  2, 
  name, 
  type, 
  'normal', 
  NOW(), 
  NOW(), 
  0
FROM t_area_check_item 
WHERE area_id = 2;

INSERT INTO t_inspection_item (record_id, name, type, status, create_time, update_time, deleted)
SELECT 
  3, 
  name, 
  type, 
  'normal', 
  NOW(), 
  NOW(), 
  0
FROM t_area_check_item 
WHERE area_id = 3; 



SELECT * FROM t_inspection_task WHERE inspector_id = (SELECT id FROM t_user WHERE username = 'admin');