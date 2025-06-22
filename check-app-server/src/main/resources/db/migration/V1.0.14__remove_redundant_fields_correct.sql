-- 正确删除t_area表中的冗余字段（不删除type字段）
-- V1.0.14__remove_redundant_fields_correct.sql

-- 1. 检查并删除uk_area_code索引（如果存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE table_schema = DATABASE() 
     AND table_name = 't_area' 
     AND index_name = 'uk_area_code') > 0,
    'ALTER TABLE `t_area` DROP INDEX `uk_area_code`',
    'SELECT "Index uk_area_code does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 删除冗余的区域编码字段 area_code（如果存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_schema = DATABASE() 
     AND table_name = 't_area' 
     AND column_name = 'area_code') > 0,
    'ALTER TABLE `t_area` DROP COLUMN `area_code`',
    'SELECT "Column area_code does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 删除冗余的区域名称字段 area_name（如果存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_schema = DATABASE() 
     AND table_name = 't_area' 
     AND column_name = 'area_name') > 0,
    'ALTER TABLE `t_area` DROP COLUMN `area_name`',
    'SELECT "Column area_name does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 注意：不删除type字段，因为实体类中有映射

-- 4. 确保保留的字段有正确的索引（如果不存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE table_schema = DATABASE() 
     AND table_name = 't_area' 
     AND index_name = 'uk_code') = 0,
    'ALTER TABLE `t_area` ADD UNIQUE KEY `uk_code` (`code`)',
    'SELECT "Index uk_code already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 