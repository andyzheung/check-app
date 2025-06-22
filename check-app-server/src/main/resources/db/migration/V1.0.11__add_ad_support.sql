-- 数据库迁移脚本：添加AD支持
-- 版本：V1.0.11
-- 日期：2025-01-27
-- 描述：为用户表添加AD集成支持字段，插入模拟AD用户数据

-- 1. 为用户表添加AD相关字段
ALTER TABLE `t_user` 
ADD COLUMN `ad_username` varchar(100) DEFAULT NULL COMMENT 'AD域账号',
ADD COLUMN `is_ad_user` tinyint DEFAULT 0 COMMENT '是否AD用户：0-本地用户，1-AD用户',
ADD COLUMN `ad_sync_time` datetime DEFAULT NULL COMMENT 'AD同步时间';

-- 2. 添加索引优化查询性能
ALTER TABLE `t_user` ADD INDEX `idx_ad_username` (`ad_username`);
ALTER TABLE `t_user` ADD INDEX `idx_is_ad_user` (`is_ad_user`);

-- 3. 插入模拟AD用户数据
INSERT INTO `t_user` (`username`, `password`, `real_name`, `role`, `status`, `phone`, `email`, `ad_username`, `is_ad_user`, `ad_sync_time`) VALUES
('zhang.san', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '张三', 'inspector', 'active', '13800138001', 'zhang.san@company.com', 'zhang.san', 1, NOW()),
('li.si', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '李四', 'inspector', 'active', '13800138002', 'li.si@company.com', 'li.si', 1, NOW()),
('wang.wu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '王五', 'admin', 'active', '13800138003', 'wang.wu@company.com', 'wang.wu', 1, NOW()),
('chen.liu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '陈六', 'inspector', 'active', '13800138004', 'chen.liu@company.com', 'chen.liu', 1, NOW()),
('zhao.qi', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '赵七', 'user', 'active', '13800138005', 'zhao.qi@company.com', 'zhao.qi', 1, NOW()),
('sun.ba', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '孙八', 'inspector', 'active', '13800138006', 'sun.ba@company.com', 'sun.ba', 1, NOW());

-- 4. 为模拟AD用户分配权限
INSERT INTO `t_user_permission` (`user_id`, `permission_code`, `permission_name`) 
SELECT u.id, 'INSPECTION_EXECUTE', '执行巡检任务' 
FROM `t_user` u 
WHERE u.role = 'inspector' AND u.is_ad_user = 1;

INSERT INTO `t_user_permission` (`user_id`, `permission_code`, `permission_name`) 
SELECT u.id, 'ADMIN_MANAGE', '管理员权限' 
FROM `t_user` u 
WHERE u.role = 'admin' AND u.is_ad_user = 1;

-- 5. 更新现有admin用户，标记为非AD用户
UPDATE `t_user` SET `is_ad_user` = 0 WHERE `username` = 'admin';

-- 6. 完善现有数据中心的模块配置（确保数据一致性）
UPDATE `t_area` SET 
    `config_json` = '{"modules": [{"id": 1, "name": "计算模块1", "type": "compute"}, {"id": 2, "name": "计算模块2", "type": "compute"}, {"id": 3, "name": "存储模块1", "type": "storage"}, {"id": 4, "name": "网络模块1", "type": "network"}]}'
WHERE `area_code` = 'DC001' AND `module_count` = 4;

UPDATE `t_area` SET 
    `config_json` = '{"modules": [{"id": 1, "name": "计算模块1", "type": "compute"}, {"id": 2, "name": "存储模块1", "type": "storage"}, {"id": 3, "name": "网络模块1", "type": "network"}]}'
WHERE `area_code` = 'DC002' AND `module_count` = 3;

UPDATE `t_area` SET 
    `config_json` = '{"modules": [{"id": 1, "name": "计算模块1", "type": "compute"}, {"id": 2, "name": "计算模块2", "type": "compute"}, {"id": 3, "name": "计算模块3", "type": "compute"}, {"id": 4, "name": "存储模块1", "type": "storage"}, {"id": 5, "name": "存储模块2", "type": "storage"}, {"id": 6, "name": "网络模块1", "type": "network"}]}'
WHERE `area_code` = 'DC003' AND `module_count` = 6;

-- 7. 添加一些巡检任务示例数据
INSERT INTO `t_inspection_task` (`task_name`, `area_id`, `inspector_id`, `plan_time`, `status`, `create_user_id`) 
SELECT 
    CONCAT(a.area_name, '定期巡检'), 
    a.id, 
    u.id, 
    DATE_ADD(NOW(), INTERVAL FLOOR(RAND() * 7) DAY),
    'pending',
    1
FROM `t_area` a 
CROSS JOIN `t_user` u 
WHERE a.area_type IN ('D', 'E') 
  AND u.role = 'inspector' 
  AND u.is_ad_user = 1
LIMIT 10; 