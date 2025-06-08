-- 插入测试用户
INSERT INTO t_user (username, password, real_name, department, role, status, create_time, update_time, deleted)
VALUES ('admin', '$2a$10$x6xqQu8fPVfOg6VQXg3z3.XwQ0TXCXQIz.VxV5.h7CFUTZpDYqYnm', '管理员', '运维部', 'admin', 'active', NOW(), NOW(), 0);

-- 插入测试区域
INSERT INTO t_inspection_area (area_code, area_name, area_type, status, description, create_time, update_time, deleted)
VALUES 
('A001', '机房A区', 'server_room', 'active', '主要服务器机房', NOW(), NOW(), 0),
('A002', '机房B区', 'server_room', 'active', '备用服务器机房', NOW(), NOW(), 0),
('A003', '配电室', 'power_room', 'active', '主配电室', NOW(), NOW(), 0);

-- 插入测试任务
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time, deleted)
VALUES 
('机房A区日常巡检', 1, 1, DATE_ADD(NOW(), INTERVAL 1 HOUR), 'pending', 1, NOW(), NOW(), 0),
('机房B区日常巡检', 2, 1, DATE_ADD(NOW(), INTERVAL 2 HOUR), 'pending', 1, NOW(), NOW(), 0),
('配电室日常巡检', 3, 1, DATE_ADD(NOW(), INTERVAL 3 HOUR), 'pending', 1, NOW(), NOW(), 0),
('机房A区周检', 1, 1, DATE_ADD(NOW(), INTERVAL -1 DAY), 'completed', 1, NOW(), NOW(), 0),
('机房B区周检', 2, 1, DATE_ADD(NOW(), INTERVAL -2 DAY), 'completed', 1, NOW(), NOW(), 0);

-- 插入测试消息
INSERT INTO t_notification (user_id, title, content, type, target_id, status, create_time, update_time, deleted)
VALUES 
(1, '新任务提醒', '您有一个新的巡检任务：机房A区日常巡检', 'task', 1, 'unread', NOW(), NOW(), 0),
(1, '任务完成提醒', '机房B区周检任务已完成', 'task', 5, 'unread', NOW(), NOW(), 0),
(1, '系统通知', '系统将于今晚22:00进行维护升级', 'system', NULL, 'unread', NOW(), NOW(), 0); 