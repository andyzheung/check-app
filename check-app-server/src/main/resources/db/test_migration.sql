-- 测试脚本，用于验证Migration执行

-- 检查数据库版本表
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 10;

-- 检查表结构
DESCRIBE t_area;
DESCRIBE t_inspection_record;
DESCRIBE t_issue_process;
DESCRIBE t_user;
DESCRIBE t_statistics_cache;
DESCRIBE t_system_param;
DESCRIBE t_file_upload;

-- 检查索引
SHOW INDEX FROM t_inspection_record;
SHOW INDEX FROM t_inspection_task;
SHOW INDEX FROM t_issue;
SHOW INDEX FROM t_notification;

-- 检查数据
SELECT COUNT(*) FROM t_statistics_cache;
SELECT COUNT(*) FROM t_system_param;
SELECT COUNT(*) FROM t_file_upload; 