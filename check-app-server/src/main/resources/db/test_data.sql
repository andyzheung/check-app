-- 插入区域数据
INSERT INTO t_area (id, code, name, description, address, type, status, create_time, update_time, deleted)
VALUES 
(1, 'AREA001', '主机房A区', '主要服务器机房', '数据中心1楼', 'server_room', 'active', NOW(), NOW(), 0),
(2, 'AREA002', '配电室B区', '主要配电室', '数据中心地下1层', 'power_room', 'active', NOW(), NOW(), 0),
(3, 'AREA003', '网络机房C区', '核心网络设备机房', '数据中心2楼', 'network_room', 'active', NOW(), NOW(), 0),
(4, 'AREA004', 'UPS机房D区', 'UPS设备间', '数据中心地下2层', 'ups_room', 'active', NOW(), NOW(), 0),
(5, 'AREA005', '监控室E区', '安防监控室', '数据中心1楼', 'monitor_room', 'active', NOW(), NOW(), 0);

-- 插入用户数据（如果还没有）
INSERT INTO t_user (id, username, password, real_name, department_id, role, status, create_time, update_time, deleted)
VALUES 
(2, 'inspector1', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '巡检员1', 2, 'user', 'active', NOW(), NOW(), 0);

-- 插入今日任务数据
INSERT INTO t_inspection_task (id, task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
-- 待完成任务
(1, '主机房A区日常巡检', 1, 2, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
(2, '配电室B区日常巡检', 2, 2, DATE_ADD(NOW(), INTERVAL 2 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
(3, '网络机房C区日常巡检', 3, 2, DATE_ADD(NOW(), INTERVAL 3 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
(4, 'UPS机房D区日常巡检', 4, 2, DATE_ADD(NOW(), INTERVAL 4 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
(5, '监控室E区日常巡检', 5, 2, DATE_ADD(NOW(), INTERVAL 5 HOUR), 'PENDING', 1, NOW(), NOW(), 0),
-- 已完成任务
(6, '主机房A区早班巡检', 1, 2, DATE_ADD(NOW(), INTERVAL -2 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0),
(7, '配电室B区早班巡检', 2, 2, DATE_ADD(NOW(), INTERVAL -3 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0),
(8, '网络机房C区早班巡检', 3, 2, DATE_ADD(NOW(), INTERVAL -4 HOUR), 'COMPLETED', 1, NOW(), NOW(), 0);

-- 插入区域巡检项模板
INSERT INTO t_area_check_item (id, area_id, name, type, sort, required, create_time, update_time, deleted)
VALUES 
-- 主机房A区巡检项
(1, 1, '温度检查', 'environment', 1, 1, NOW(), NOW(), 0),
(2, 1, '湿度检查', 'environment', 2, 1, NOW(), NOW(), 0),
(3, 1, '空调运行状态', 'environment', 3, 1, NOW(), NOW(), 0),
(4, 1, '消防设备检查', 'security', 4, 1, NOW(), NOW(), 0),
(5, 1, '服务器运行状态', 'device', 5, 1, NOW(), NOW(), 0),
-- 配电室B区巡检项
(6, 2, '配电柜检查', 'device', 1, 1, NOW(), NOW(), 0),
(7, 2, '电压检查', 'device', 2, 1, NOW(), NOW(), 0),
(8, 2, '电流检查', 'device', 3, 1, NOW(), NOW(), 0),
(9, 2, '接地系统检查', 'security', 4, 1, NOW(), NOW(), 0),
-- 网络机房C区巡检项
(10, 3, '网络设备运行状态', 'device', 1, 1, NOW(), NOW(), 0),
(11, 3, '网络连接检查', 'device', 2, 1, NOW(), NOW(), 0),
(12, 3, '温度检查', 'environment', 3, 1, NOW(), NOW(), 0),
(13, 3, '湿度检查', 'environment', 4, 1, NOW(), NOW(), 0),
-- UPS机房D区巡检项
(14, 4, 'UPS运行状态', 'device', 1, 1, NOW(), NOW(), 0),
(15, 4, '电池组检查', 'device', 2, 1, NOW(), NOW(), 0),
(16, 4, '输出电压检查', 'device', 3, 1, NOW(), NOW(), 0),
-- 监控室E区巡检项
(17, 5, '监控设备运行状态', 'device', 1, 1, NOW(), NOW(), 0),
(18, 5, '录像存储检查', 'device', 2, 1, NOW(), NOW(), 0),
(19, 5, '监控画面检查', 'device', 3, 1, NOW(), NOW(), 0); 