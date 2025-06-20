-- 数据库迁移脚本 V1.0.9
-- 添加缺失的route_data字段到t_inspection_record表

-- 检查route_data字段是否已存在，如果不存在则添加
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_inspection_record' AND column_name = 'route_data' AND table_schema = DATABASE();

SET @addRouteDataCol = CONCAT('ALTER TABLE t_inspection_record ADD COLUMN route_data TEXT DEFAULT NULL COMMENT ', CHAR(39), '巡检路径数据（JSON格式存储）', CHAR(39));
SET @statement = IF(@colExists = 0, @addRouteDataCol, 'SELECT 1');

PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 验证字段是否添加成功
SELECT 'route_data字段添加完成' AS result; 