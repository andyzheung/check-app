-- 数据库扩展脚本 V1.0.6
-- 根据design/database-extension.md进行扩展

-- 1. 扩展t_area表 (检查qr_code_url不存在才添加)
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_area' AND column_name = 'qr_code_url' AND table_schema = DATABASE();
SET @addAreaQrCol = CONCAT('ALTER TABLE t_area ADD COLUMN qr_code_url VARCHAR(255) DEFAULT NULL COMMENT ', CHAR(39), '区域二维码URL', CHAR(39));
SET @statement = IF(@colExists = 0, @addAreaQrCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 扩展t_inspection_record表 (检查route_path不存在才添加)
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_inspection_record' AND column_name = 'route_path' AND table_schema = DATABASE();
SET @addRecordRouteCol = CONCAT('ALTER TABLE t_inspection_record ADD COLUMN route_path TEXT DEFAULT NULL COMMENT ', CHAR(39), '巡检路径（JSON格式存储）', CHAR(39));
SET @statement = IF(@colExists = 0, @addRecordRouteCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 扩展t_issue_process表 (检查images不存在才添加)
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_issue_process' AND column_name = 'images' AND table_schema = DATABASE();
SET @addProcessImagesCol = CONCAT('ALTER TABLE t_issue_process ADD COLUMN images TEXT DEFAULT NULL COMMENT ', CHAR(39), '图片（JSON数组存储多张图片URL）', CHAR(39));
SET @statement = IF(@colExists = 0, @addProcessImagesCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 新增统计数据缓存表
CREATE TABLE IF NOT EXISTS t_statistics_cache (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cache_key` VARCHAR(100) NOT NULL COMMENT '缓存键',
  `cache_data` TEXT NOT NULL COMMENT '缓存数据（JSON格式）',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_key` (`cache_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据缓存表';

-- 5. 新增系统参数表
CREATE TABLE IF NOT EXISTS t_system_param (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `param_key` VARCHAR(100) NOT NULL COMMENT '参数键',
  `param_value` VARCHAR(500) NOT NULL COMMENT '参数值',
  `param_desc` VARCHAR(200) DEFAULT NULL COMMENT '参数描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统参数表';

-- 6. 扩展t_user表 (检查avatar不存在才添加)
SET @colExists = 0;
SELECT COUNT(*) INTO @colExists FROM information_schema.columns 
WHERE table_name = 't_user' AND column_name = 'avatar' AND table_schema = DATABASE();
SET @addUserAvatarCol = CONCAT('ALTER TABLE t_user ADD COLUMN avatar VARCHAR(255) DEFAULT NULL COMMENT ', CHAR(39), '用户头像URL', CHAR(39));
SET @statement = IF(@colExists = 0, @addUserAvatarCol, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 7. 新增文件上传表
CREATE TABLE IF NOT EXISTS t_file_upload (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` VARCHAR(100) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(255) NOT NULL COMMENT '文件路径',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
  `upload_user_id` BIGINT NOT NULL COMMENT '上传用户ID',
  `business_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
  `business_id` BIGINT DEFAULT NULL COMMENT '业务ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件上传表';

-- 8. 索引优化 (检查索引是否存在)
-- 巡检记录表索引优化
SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_inspection_record' AND index_name = 'idx_area_inspector_time';
SET @addIdx1 = 'ALTER TABLE t_inspection_record ADD INDEX idx_area_inspector_time (area_id, inspector_id, start_time)';
SET @statement = IF(@idxExists = 0, @addIdx1, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_inspection_record' AND index_name = 'idx_task_id';
SET @addIdx2 = 'ALTER TABLE t_inspection_record ADD INDEX idx_task_id (task_id)';
SET @statement = IF(@idxExists = 0, @addIdx2, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 巡检任务表索引优化
SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_inspection_task' AND index_name = 'idx_area_inspector_time';
SET @addIdx3 = 'ALTER TABLE t_inspection_task ADD INDEX idx_area_inspector_time (area_id, inspector_id, plan_time)';
SET @statement = IF(@idxExists = 0, @addIdx3, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_inspection_task' AND index_name = 'idx_status';
SET @addIdx4 = 'ALTER TABLE t_inspection_task ADD INDEX idx_status (status)';
SET @statement = IF(@idxExists = 0, @addIdx4, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 问题表索引优化
SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_issue' AND index_name = 'idx_record_type_status';
SET @addIdx5 = 'ALTER TABLE t_issue ADD INDEX idx_record_type_status (record_id, type, status)';
SET @statement = IF(@idxExists = 0, @addIdx5, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_issue' AND index_name = 'idx_handler_id';
SET @addIdx6 = 'ALTER TABLE t_issue ADD INDEX idx_handler_id (handler_id)';
SET @statement = IF(@idxExists = 0, @addIdx6, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 消息通知表索引优化
SELECT COUNT(*) INTO @idxExists FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 't_notification' AND index_name = 'idx_user_type_status';
SET @addIdx7 = 'ALTER TABLE t_notification ADD INDEX idx_user_type_status (user_id, type, status)';
SET @statement = IF(@idxExists = 0, @addIdx7, 'SELECT 1');
PREPARE stmt FROM @statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 9. 字符集与排序规则统一
-- 设置数据库字符集
ALTER DATABASE check_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 