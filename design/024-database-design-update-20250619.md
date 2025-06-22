# 巡检系统数据库设计更新文档

**更新日期**: 2025-01-27  
**版本**: v2.0  
**更新内容**: 支持数据中心和弱电间两种巡检类型

## 1. 概述

本文档基于现有数据库结构，扩展支持数据中心和弱电间两种不同类型的巡检需求。设计遵循最小改动原则，尽可能复用现有表结构。

## 2. 表结构设计

### 2.1 区域表 (t_area) - 扩展

现有表结构基础上增加区域类型相关字段：

```sql
ALTER TABLE t_area ADD COLUMN area_type ENUM('datacenter', 'weakroom') NOT NULL DEFAULT 'datacenter' COMMENT '区域类型：datacenter-数据中心，weakroom-弱电间';
ALTER TABLE t_area ADD COLUMN module_count INT DEFAULT 0 COMMENT '模块数量（仅数据中心使用）';
ALTER TABLE t_area ADD COLUMN config_json TEXT COMMENT '区域配置信息（JSON格式）';
```

完整表结构：
```sql
CREATE TABLE t_area (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '区域ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '区域编码',
    name VARCHAR(100) NOT NULL COMMENT '区域名称',
    description TEXT COMMENT '区域描述',
    address VARCHAR(200) COMMENT '区域地址',
    type VARCHAR(50) COMMENT '区域分类（预留字段）',
    area_type ENUM('datacenter', 'weakroom') NOT NULL DEFAULT 'datacenter' COMMENT '区域类型：datacenter-数据中心，weakroom-弱电间',
    module_count INT DEFAULT 0 COMMENT '模块数量（仅数据中心使用）',
    config_json TEXT COMMENT '区域配置信息（JSON格式）',
    status VARCHAR(20) DEFAULT 'active' COMMENT '区域状态：active-活跃，inactive-未激活',
    qr_code_url VARCHAR(500) COMMENT '区域二维码URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
);
```

### 2.2 巡检项目配置表 (t_inspection_item) - 新增

```sql
CREATE TABLE t_inspection_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '巡检项目ID',
    area_type ENUM('datacenter', 'weakroom') NOT NULL COMMENT '适用区域类型',
    item_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    item_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    item_type ENUM('boolean', 'number', 'text', 'select') NOT NULL COMMENT '项目类型：boolean-是否，number-数值，text-文本，select-选择',
    options_json TEXT COMMENT '选项配置（JSON格式，用于select类型）',
    is_required TINYINT DEFAULT 1 COMMENT '是否必填：0-否，1-是',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    group_name VARCHAR(50) COMMENT '分组名称',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-启用，inactive-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_area_type_item_code (area_type, item_code)
);
```

### 2.3 巡检记录详情表 (t_inspection_record_detail) - 新增

```sql
CREATE TABLE t_inspection_record_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录详情ID',
    record_id BIGINT NOT NULL COMMENT '巡检记录ID',
    item_id BIGINT NOT NULL COMMENT '巡检项目ID',
    item_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    item_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    item_value TEXT COMMENT '项目值',
    is_normal TINYINT COMMENT '是否正常：0-异常，1-正常，NULL-不适用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_record_id (record_id),
    INDEX idx_item_code (item_code),
    FOREIGN KEY (record_id) REFERENCES t_inspection_record(id) ON DELETE CASCADE
);
```

### 2.4 巡检记录表 (t_inspection_record) - 扩展

现有表结构基础上增加区域类型字段：

```sql
ALTER TABLE t_inspection_record ADD COLUMN area_type ENUM('datacenter', 'weakroom') COMMENT '区域类型';
ALTER TABLE t_inspection_record ADD COLUMN total_items INT DEFAULT 0 COMMENT '总项目数';
ALTER TABLE t_inspection_record ADD COLUMN normal_items INT DEFAULT 0 COMMENT '正常项目数';
ALTER TABLE t_inspection_record ADD COLUMN abnormal_items INT DEFAULT 0 COMMENT '异常项目数';
```

## 3. 初始化数据

### 3.1 数据中心巡检项目配置

```sql
INSERT INTO t_inspection_item (area_type, item_code, item_name, item_type, group_name, sort_order) VALUES
('datacenter', 'sound_light_alarm', '机房范围内是否有声光报警', 'boolean', '安全检查', 1),
('datacenter', 'fire_load_check', '机房内是否有纸箱、泡沫等火载量', 'boolean', '安全检查', 2),
('datacenter', 'room_leakage', '机房内是否有漏水', 'boolean', '安全检查', 3),
('datacenter', 'ceiling_leakage', '天花是否有漏水', 'boolean', '安全检查', 4),
('datacenter', 'room_temperature', '机房环境温度', 'number', '环境检查', 5);

-- 模块相关项目需要根据具体模块数量动态生成
-- 例如：模块1回风温度、模块1供电状态等
```

### 3.2 弱电间巡检项目配置

```sql
INSERT INTO t_inspection_item (area_type, item_code, item_name, item_type, group_name, sort_order) VALUES
('weakroom', 'floor_leakage', '机房地面是否漏水', 'boolean', '漏水检查', 1),
('weakroom', 'wall_leakage', '墙壁是否漏水', 'boolean', '漏水检查', 2),
('weakroom', 'ceiling_leakage', '天花是否漏水', 'boolean', '漏水检查', 3),
('weakroom', 'combustible_check', '机房是否有垃圾纸箱等可燃物', 'boolean', '安全检查', 4),
('weakroom', 'room_temperature', '机房环境温度', 'number', '环境检查', 5),
('weakroom', 'mains_power', '市电供电是否正常', 'boolean', '供电检查', 6),
('weakroom', 'ups_power', 'UPS供电是否正常', 'boolean', '供电检查', 7),
('weakroom', 'cabinet_lock', '网络机柜是否正常上锁', 'boolean', '设备检查', 8),
('weakroom', 'access_control', '机房门禁是否正常', 'boolean', '设备检查', 9),
('weakroom', 'equipment_noise', '机房设备运行是否有异响', 'boolean', '设备检查', 10),
('weakroom', 'lighting_check', '机房照明是否正常', 'boolean', '设备检查', 11);
```

## 4. 数据迁移策略

### 4.1 现有数据兼容性

1. 现有区域数据默认设置为数据中心类型
2. 现有巡检记录保持不变，通过区域关联获取类型信息

```sql
-- 更新现有区域为数据中心类型
UPDATE t_area SET area_type = 'datacenter' WHERE area_type IS NULL;

-- 更新现有巡检记录的区域类型
UPDATE t_inspection_record r 
JOIN t_area a ON r.area_id = a.id 
SET r.area_type = a.area_type 
WHERE r.area_type IS NULL;
```

### 4.2 历史数据处理

对于现有的巡检记录，可以通过数据转换脚本将原有的JSON格式数据转换为新的详情表结构。

## 5. 索引优化

```sql
-- 巡检记录表索引
CREATE INDEX idx_area_type_create_time ON t_inspection_record(area_type, create_time);
CREATE INDEX idx_area_id_area_type ON t_inspection_record(area_id, area_type);

-- 巡检项目配置表索引
CREATE INDEX idx_area_type_group_sort ON t_inspection_item(area_type, group_name, sort_order);

-- 巡检记录详情表索引
CREATE INDEX idx_record_item ON t_inspection_record_detail(record_id, item_code);
```

## 6. 扩展性设计

### 6.1 支持动态模块配置

对于数据中心的模块检查，支持通过配置表动态生成巡检项目：

```sql
-- 区域模块配置示例（存储在 t_area.config_json 中）
{
  "modules": [
    {
      "id": 1,
      "name": "模块1",
      "items": ["return_air_temp", "power_status"]
    },
    {
      "id": 2,
      "name": "模块2", 
      "items": ["return_air_temp", "power_status"]
    }
  ]
}
```

### 6.2 支持新增区域类型

通过修改 ENUM 类型可以轻松支持新的区域类型：

```sql
ALTER TABLE t_area MODIFY area_type ENUM('datacenter', 'weakroom', 'newtype');
ALTER TABLE t_inspection_item MODIFY area_type ENUM('datacenter', 'weakroom', 'newtype');
```

## 7. 性能考虑

1. **分表策略**: 如果巡检记录详情数据量很大，可以考虑按月分表
2. **缓存策略**: 巡检项目配置可以缓存到Redis中，减少数据库查询
3. **批量插入**: 巡检记录详情支持批量插入，提高性能

## 8. 备份与恢复

建议增加定期备份策略，特别是新增的配置表和详情表：

```bash
# 备份配置表
mysqldump -u username -p database_name t_inspection_item > inspection_item_backup.sql

# 备份详情表（按月）
mysqldump -u username -p database_name t_inspection_record_detail --where="create_time >= '2025-01-01'" > detail_backup_202501.sql
```

## 9. 监控指标

建议增加以下监控指标：
- 各区域类型的巡检完成率
- 异常项目统计（按区域类型）
- 巡检记录详情表的增长趋势
- 配置项目的使用频率统计 