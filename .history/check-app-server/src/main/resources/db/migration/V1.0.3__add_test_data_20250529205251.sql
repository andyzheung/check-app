-- 插入测试任务
INSERT INTO t_inspection_task (name, description, plan_time, point_id, inspector_id, status, remark, create_time, update_time, deleted)
VALUES 
('机房A区日常巡检', '主要服务器机房日常巡检', DATE_ADD(NOW(), INTERVAL 1 HOUR), 1, 1, 'PENDING', NULL, NOW(), NOW(), 0),
('机房B区日常巡检', '备用服务器机房日常巡检', DATE_ADD(NOW(), INTERVAL 2 HOUR), 2, 1, 'PENDING', NULL, NOW(), NOW(), 0),
('配电室日常巡检', '主配电室日常巡检', DATE_ADD(NOW(), INTERVAL 3 HOUR), 3, 1, 'PENDING', NULL, NOW(), NOW(), 0),
('机房A区周检', '主要服务器机房周检', DATE_ADD(NOW(), INTERVAL -1 DAY), 1, 1, 'COMPLETED', '已完成周检', NOW(), NOW(), 0),
('机房B区周检', '备用服务器机房周检', DATE_ADD(NOW(), INTERVAL -2 DAY), 2, 1, 'COMPLETED', '已完成周检', NOW(), NOW(), 0);

-- 插入测试消息
INSERT INTO t_notification (user_id, title, content, type, target_id, status, create_time, update_time, deleted)
VALUES 
(1, '新任务提醒', '您有一个新的巡检任务：机房A区日常巡检', 'task', 1, 'unread', NOW(), NOW(), 0),
(1, '任务完成提醒', '机房B区周检任务已完成', 'task', 5, 'unread', NOW(), NOW(), 0),
(1, '系统通知', '系统将于今晚22:00进行维护升级', 'system', NULL, 'unread', NOW(), NOW(), 0); 