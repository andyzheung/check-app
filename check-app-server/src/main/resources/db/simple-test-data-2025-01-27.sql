-- 简单测试数据SQL - 2025-01-27
-- 专门用于测试数据中心3(DC004)和弱电间15(WE015)的功能
-- 使用输入编码的方式进行测试，无需扫码

-- 🧹 1. 清理今日测试数据
DELETE FROM `t_inspection_item` WHERE `record_id` IN (SELECT id FROM `t_inspection_record` WHERE DATE(`start_time`) = CURDATE());
DELETE FROM `t_inspection_record` WHERE DATE(`start_time`) = CURDATE();
DELETE FROM `t_inspection_task` WHERE DATE(`plan_time`) = CURDATE();
DELETE FROM `t_statistics_cache` WHERE `cache_key` LIKE '%today%';

-- 🎯 2. 插入今日巡检任务 (用户ID=9)
INSERT INTO `t_inspection_task` (`task_name`, `area_id`, `inspector_id`, `plan_time`, `status`, `create_user_id`, `create_time`, `update_time`, `deleted`) VALUES
-- 今日任务
('数据中心3日常巡检', 34, 9, CONCAT(CURDATE(), ' 09:00:00'), 'PENDING', 1, NOW(), NOW(), 0),
('数据中心3温度检查', 34, 9, CONCAT(CURDATE(), ' 14:00:00'), 'PENDING', 1, NOW(), NOW(), 0),
('弱电间15安全检查', 35, 9, CONCAT(CURDATE(), ' 10:30:00'), 'COMPLETED', 1, NOW(), NOW(), 0),
('弱电间15环境检查', 35, 9, CONCAT(CURDATE(), ' 16:00:00'), 'PENDING', 1, NOW(), NOW(), 0);

-- 🔍 3. 插入已完成的巡检记录
INSERT INTO `t_inspection_record` (`record_no`, `task_id`, `area_id`, `inspector_id`, `start_time`, `end_time`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
-- 弱电间15的巡检记录
('R20250127001', 
 (SELECT id FROM `t_inspection_task` WHERE `task_name` = '弱电间15安全检查' AND DATE(`plan_time`) = CURDATE() LIMIT 1),
 35, 9, CONCAT(CURDATE(), ' 10:30:00'), CONCAT(CURDATE(), ' 11:00:00'), 'normal', '弱电间15巡检完成，设备正常', NOW(), NOW(), 0);

-- 🔧 4. 为巡检记录添加详细巡检项
SET @record_id = (SELECT id FROM `t_inspection_record` WHERE `record_no` = 'R20250127001' LIMIT 1);

INSERT INTO `t_inspection_item` (`record_id`, `name`, `type`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
-- 弱电间巡检项（按照UI原型设计）
-- 漏水检查
(@record_id, '机房地面是否漏水', 'environment', 'normal', '地面无漏水', NOW(), NOW(), 0),
(@record_id, '墙壁是否漏水', 'environment', 'normal', '墙壁无漏水', NOW(), NOW(), 0),
(@record_id, '天花是否漏水', 'environment', 'normal', '天花无漏水', NOW(), NOW(), 0),
-- 安全检查
(@record_id, '机房是否有垃圾纸箱等可燃物', 'security', 'normal', '无可燃物', NOW(), NOW(), 0),
-- 环境检查
(@record_id, '机房环境温度', 'environment', 'normal', '温度正常：22°C', NOW(), NOW(), 0),
-- 供电检查
(@record_id, '市电供电是否正常', 'device', 'normal', '市电供电正常', NOW(), NOW(), 0),
(@record_id, 'UPS供电是否正常', 'device', 'normal', 'UPS供电正常', NOW(), NOW(), 0),
-- 设备检查
(@record_id, '网络机柜是否正常上锁', 'device', 'normal', '机柜正常上锁', NOW(), NOW(), 0),
(@record_id, '机房门禁是否正常', 'device', 'normal', '门禁系统正常', NOW(), NOW(), 0),
(@record_id, '机房设备运行是否有异响', 'device', 'normal', '设备运行无异响', NOW(), NOW(), 0),
(@record_id, '机房照明是否正常', 'device', 'normal', '照明系统正常', NOW(), NOW(), 0);

-- 🔧 5. 添加数据中心3的历史巡检记录 (用于测试详情显示)
INSERT INTO `t_inspection_record` (`record_no`, `task_id`, `area_id`, `inspector_id`, `start_time`, `end_time`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
('R20250126001', NULL, 34, 9, 
 CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 09:00:00'), 
 CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 09:45:00'), 
 'normal', '数据中心3昨日巡检正常', NOW(), NOW(), 0);

-- 为数据中心3巡检记录添加详细巡检项
SET @dc_record_id = (SELECT id FROM `t_inspection_record` WHERE `record_no` = 'R20250126001' LIMIT 1);

INSERT INTO `t_inspection_item` (`record_id`, `name`, `type`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
-- 数据中心巡检项（按照UI原型设计）
-- 安全检查
(@dc_record_id, '机房范围内是否有声光报警', 'security', 'normal', '声光报警设备正常', NOW(), NOW(), 0),
(@dc_record_id, '机房内是否有纸箱、泡沫等火载量', 'security', 'normal', '无火载量物品', NOW(), NOW(), 0),
(@dc_record_id, '机房内是否有漏水', 'security', 'normal', '机房内无漏水', NOW(), NOW(), 0),
(@dc_record_id, '天花是否有漏水', 'security', 'normal', '天花无漏水', NOW(), NOW(), 0),
-- 环境检查
(@dc_record_id, '机房环境温度', 'environment', 'normal', '温度正常：24°C', NOW(), NOW(), 0),
-- 模块检查（数据中心3有10个模块）
(@dc_record_id, '模块1回风温度', 'device', 'normal', '回风温度：26°C', NOW(), NOW(), 0),
(@dc_record_id, '模块1供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块2回风温度', 'device', 'normal', '回风温度：25°C', NOW(), NOW(), 0),
(@dc_record_id, '模块2供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块3回风温度', 'device', 'abnormal', '回风温度：32°C 过高', NOW(), NOW(), 0),
(@dc_record_id, '模块3供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块4回风温度', 'device', 'normal', '回风温度：27°C', NOW(), NOW(), 0),
(@dc_record_id, '模块4供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块5回风温度', 'device', 'normal', '回风温度：26°C', NOW(), NOW(), 0),
(@dc_record_id, '模块5供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块6回风温度', 'device', 'normal', '回风温度：25°C', NOW(), NOW(), 0),
(@dc_record_id, '模块6供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块7回风温度', 'device', 'normal', '回风温度：28°C', NOW(), NOW(), 0),
(@dc_record_id, '模块7供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块8回风温度', 'device', 'normal', '回风温度：27°C', NOW(), NOW(), 0),
(@dc_record_id, '模块8供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块9回风温度', 'device', 'normal', '回风温度：26°C', NOW(), NOW(), 0),
(@dc_record_id, '模块9供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0),
(@dc_record_id, '模块10回风温度', 'device', 'normal', '回风温度：25°C', NOW(), NOW(), 0),
(@dc_record_id, '模块10供电是否正常', 'device', 'normal', '供电正常', NOW(), NOW(), 0);

-- 🔧 6. 更新任务状态
UPDATE `t_inspection_task` SET `status` = 'COMPLETED' 
WHERE `task_name` = '弱电间15安全检查' AND DATE(`plan_time`) = CURDATE();

-- 🔧 7. 清理缓存
DELETE FROM `t_statistics_cache` WHERE `cache_key` LIKE '%today%' OR `cache_key` LIKE '%daily%';

-- ✅ 测试数据创建完成！
-- 现在你有以下测试数据（完全按照UI原型设计）：
-- 📊 今日任务统计：4个任务 (1个已完成，3个待完成)
-- 🏢 数据中心3(DC004)：有历史巡检记录，包含25个巡检项
--    - 安全检查：4项 (全部正常)
--    - 环境检查：1项 (正常)
--    - 模块检查：20项 (19项正常，1项异常 - 模块3回风温度过高)
-- 🔌 弱电间15(WE015)：有今日巡检记录，包含11个巡检项
--    - 漏水检查：3项 (全部正常)
--    - 安全检查：1项 (正常)
--    - 环境检查：1项 (正常)
--    - 供电检查：2项 (全部正常)
--    - 设备检查：4项 (全部正常) 