# 巡检系统数据库扩展设计

## 1. 概述

本文档描述了针对巡检系统的数据库扩展设计，遵循最小修改原则，在现有数据库结构基础上进行扩展。

## 2. 现有数据库结构

系统当前使用以下主要数据表：

1. **t_user** - 用户表
2. **t_user_permission** - 用户权限表
3. **t_department** - 部门表
4. **t_area** - 区域表
5. **t_area_type** - 区域类型表
6. **t_area_check_item** - 区域巡检项模板表
7. **t_inspection_task** - 巡检任务表
8. **t_inspection_record** - 巡检记录表
9. **t_inspection_item** - 巡检项表
10. **t_issue** - 问题表
11. **t_issue_process** - 问题处理记录表
12. **t_notification** - 消息通知表

## 3. 建议的扩展和修改

以下是建议的数据库扩展和少量修改，以支持新功能和优化现有功能：

### 3.1 扩展t_area表

为t_area表添加二维码URL字段，以支持区域二维码功能：

```sql
ALTER TABLE t_area ADD COLUMN qr_code_url VARCHAR(255) DEFAULT NULL COMMENT '区域二维码URL';
```

### 3.2 扩展t_inspection_record表

为t_inspection_record表添加路径字段，以支持巡检路径记录：

```sql
ALTER TABLE t_inspection_record ADD COLUMN route_path TEXT DEFAULT NULL COMMENT '巡检路径（JSON格式存储）';
```

### 3.3 扩展t_issue_process表

为t_issue_process表添加图片字段，以支持问题处理过程中的图片上传：

```sql
ALTER TABLE t_issue_process ADD COLUMN images TEXT DEFAULT NULL COMMENT '图片（JSON数组存储多张图片URL）';
```

### 3.4 新增统计数据缓存表

为提高统计数据查询性能，建议新增统计数据缓存表：

```sql
CREATE TABLE t_statistics_cache (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cache_key` VARCHAR(100) NOT NULL COMMENT '缓存键',
  `cache_data` TEXT NOT NULL COMMENT '缓存数据（JSON格式）',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_key` (`cache_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据缓存表';
```

### 3.5 新增系统参数表

为支持系统参数配置，建议新增系统参数表：

```sql
CREATE TABLE t_system_param (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `param_key` VARCHAR(100) NOT NULL COMMENT '参数键',
  `param_value` VARCHAR(500) NOT NULL COMMENT '参数值',
  `param_desc` VARCHAR(200) DEFAULT NULL COMMENT '参数描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统参数表';
```

### 3.6 扩展t_user表

为t_user表添加头像字段，以支持用户头像功能：

```sql
ALTER TABLE t_user ADD COLUMN avatar VARCHAR(255) DEFAULT NULL COMMENT '用户头像URL';
```

### 3.7 新增文件上传表

为支持文件上传功能，建议新增文件上传表：

```sql
CREATE TABLE t_file_upload (
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
```

## 4. 索引优化建议

为提高查询性能，建议添加以下索引：

```sql
-- 巡检记录表索引优化
ALTER TABLE t_inspection_record ADD INDEX idx_area_inspector_time (area_id, inspector_id, start_time);
ALTER TABLE t_inspection_record ADD INDEX idx_task_id (task_id);

-- 巡检任务表索引优化
ALTER TABLE t_inspection_task ADD INDEX idx_area_inspector_time (area_id, inspector_id, plan_time);
ALTER TABLE t_inspection_task ADD INDEX idx_status (status);

-- 问题表索引优化
ALTER TABLE t_issue ADD INDEX idx_record_type_status (record_id, type, status);
ALTER TABLE t_issue ADD INDEX idx_handler_id (handler_id);

-- 消息通知表索引优化
ALTER TABLE t_notification ADD INDEX idx_user_type_status (user_id, type, status);
```

## 5. 字符集与排序规则

为确保所有表使用一致的字符集和排序规则，建议统一设置为utf8mb4和utf8mb4_unicode_ci：

```sql
-- 设置数据库字符集
ALTER DATABASE check_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 对所有表执行
ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 6. 结论

以上建议的数据库扩展和修改保持了对现有数据库结构的最小改动原则，同时提供了支持新功能和优化性能的必要变更。这些修改可以逐步实施，不会影响系统的正常运行。