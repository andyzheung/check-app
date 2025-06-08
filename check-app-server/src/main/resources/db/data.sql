-- 插入区域类型数据
INSERT INTO t_area_type (type_code, type_name, description, create_time, update_time, deleted) VALUES
('A', '机房区域', '数据中心机房区域', NOW(), NOW(), 0),
('B', '办公区', '日常办公区域', NOW(), NOW(), 0),
('C', '设备区', '设备存放和维护区域', NOW(), NOW(), 0);

-- 插入区域数据
INSERT INTO t_area (code, name, type, description, status, create_time, update_time, deleted) VALUES
('AREAB001', '主办公区', 'B', '主要办公区域，包含财务部和人事部', 'active', NOW(), NOW(), 0),
('AREAA001', '主机房', 'A', '主要机房区域，存放核心服务器', 'active', NOW(), NOW(), 0),
('AREAC001', '设备间', 'C', '网络设备存放区域', 'active', NOW(), NOW(), 0); 