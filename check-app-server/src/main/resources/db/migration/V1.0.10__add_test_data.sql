-- 数据库迁移脚本 V1.0.10
-- 添加测试数据用于验证首页功能

-- 插入测试任务数据（今日任务）
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time) VALUES
('数据中心日常巡检', 1, 1, CONCAT(CURDATE(), ' 09:00:00'), 'PENDING', 1, NOW(), NOW()),
('弱电间设备检查', 2, 1, CONCAT(CURDATE(), ' 14:00:00'), 'PENDING', 1, NOW(), NOW()),
('机房环境监测', 3, 1, CONCAT(CURDATE(), ' 16:00:00'), 'COMPLETED', 1, NOW(), NOW()),
('网络设备巡检', 1, 1, CONCAT(CURDATE(), ' 10:30:00'), 'COMPLETED', 1, NOW(), NOW()),
('安全检查任务', 2, 1, CONCAT(CURDATE(), ' 13:00:00'), 'PENDING', 1, NOW(), NOW());

-- 插入昨日任务数据（用于对比）
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_user_id, create_time, update_time) VALUES
('昨日数据中心巡检', 1, 1, CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 09:00:00'), 'COMPLETED', 1, NOW(), NOW()),
('昨日弱电间检查', 2, 1, CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 14:00:00'), 'COMPLETED', 1, NOW(), NOW());

-- 验证数据插入
SELECT '测试数据插入完成' AS result;
SELECT COUNT(*) AS today_tasks FROM t_inspection_task WHERE DATE(plan_time) = CURDATE();
SELECT COUNT(*) AS pending_tasks FROM t_inspection_task WHERE DATE(plan_time) = CURDATE() AND status = 'PENDING';
SELECT COUNT(*) AS completed_tasks FROM t_inspection_task WHERE DATE(plan_time) = CURDATE() AND status = 'COMPLETED'; 