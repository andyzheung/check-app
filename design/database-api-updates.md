# 数据库扩展对API接口的影响

## 概述

本文档详细说明数据库扩展对现有API接口的影响，以及需要新增的接口，用于支持扩展功能。

## 1. 数据库扩展回顾

数据库扩展主要包括以下内容：

1. `t_area`表增加`qr_code_url`字段 - 用于存储区域二维码URL
2. `t_inspection_record`表增加`route_path`字段 - 用于存储巡检路径（JSON格式）
3. `t_issue_process`表增加`images`字段 - 用于存储问题处理的多张图片URL（JSON数组）
4. `t_user`表增加`avatar`字段 - 用于存储用户头像URL
5. 新增`t_statistics_cache`表 - 用于缓存统计数据
6. 新增`t_system_param`表 - 用于存储系统参数
7. 新增`t_file_upload`表 - 用于管理文件上传

## 2. 需要更新的现有接口

### 2.1 用户相关接口

#### 获取用户详情接口 
**路径**: `GET /api/v1/users/{id}`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`avatar`字段  
**修改前**:
```json
{
  "id": 1,
  "username": "admin",
  "realName": "系统管理员",
  "departmentId": 1,
  "departmentName": "IT部门",
  "role": "admin",
  "status": "active",
  "phone": "13800138000",
  "email": "admin@example.com",
  "lastLoginTime": "2025-06-10 10:00:00",
  "createTime": "2025-01-01 00:00:00"
}
```

**修改后**:
```json
{
  "id": 1,
  "username": "admin",
  "realName": "系统管理员",
  "departmentId": 1,
  "departmentName": "IT部门",
  "role": "admin",
  "status": "active",
  "phone": "13800138000",
  "email": "admin@example.com",
  "avatar": "https://api.example.com/avatars/default-1.png",
  "lastLoginTime": "2025-06-10 10:00:00",
  "createTime": "2025-01-01 00:00:00"
}
```

#### 获取用户列表接口
**路径**: `GET /api/v1/users`  
**分类**: [ADMIN]  
**修改内容**: 响应中增加`avatar`字段  

#### 获取当前用户信息接口
**路径**: `GET /api/v1/auth/info`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`avatar`字段  

### 2.2 区域相关接口

#### 获取区域详情接口
**路径**: `GET /api/v1/areas/{id}`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`qrCodeUrl`字段  
**修改前**:
```json
{
  "id": 1,
  "code": "AREA101",
  "name": "主机房A区",
  "description": "主要服务器设备机房",
  "address": "数据中心1楼",
  "type": "server",
  "status": "active",
  "createTime": "2025-01-01 00:00:00"
}
```

**修改后**:
```json
{
  "id": 1,
  "code": "AREA101",
  "name": "主机房A区",
  "description": "主要服务器设备机房",
  "address": "数据中心1楼",
  "type": "server",
  "status": "active",
  "qrCodeUrl": "https://api.example.com/qrcode/area/1",
  "createTime": "2025-01-01 00:00:00"
}
```

#### 获取区域列表接口
**路径**: `GET /api/v1/areas`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`qrCodeUrl`字段  

### 2.3 巡检记录相关接口

#### 获取巡检记录详情接口
**路径**: `GET /api/v1/records/{id}`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`routePath`字段  
**修改前**:
```json
{
  "id": 1,
  "recordNo": "REC20250601001",
  "taskId": 1,
  "areaId": 1,
  "areaName": "主机房A区",
  "inspectorId": 2,
  "inspectorName": "李四",
  "startTime": "2025-06-01 09:00:00",
  "endTime": "2025-06-01 10:30:00",
  "status": "completed",
  "remark": "正常巡检，未发现异常",
  "createTime": "2025-06-01 09:00:00"
}
```

**修改后**:
```json
{
  "id": 1,
  "recordNo": "REC20250601001",
  "taskId": 1,
  "areaId": 1,
  "areaName": "主机房A区",
  "inspectorId": 2,
  "inspectorName": "李四",
  "startTime": "2025-06-01 09:00:00",
  "endTime": "2025-06-01 10:30:00",
  "status": "completed",
  "remark": "正常巡检，未发现异常",
  "routePath": [
    {"timestamp": "2025-06-01 09:00:00", "latitude": 30.65984, "longitude": 104.06538, "location": "入口"},
    {"timestamp": "2025-06-01 09:05:00", "latitude": 30.65990, "longitude": 104.06545, "location": "设备区1"},
    {"timestamp": "2025-06-01 09:10:00", "latitude": 30.66000, "longitude": 104.06550, "location": "设备区2"},
    {"timestamp": "2025-06-01 10:30:00", "latitude": 30.65984, "longitude": 104.06538, "location": "出口"}
  ],
  "createTime": "2025-06-01 09:00:00"
}
```

### 2.4 问题处理相关接口

#### 获取问题处理记录接口
**路径**: `GET /api/v1/issues/{id}/processes`  
**分类**: [COMMON]  
**修改内容**: 响应中增加`images`字段  
**修改前**:
```json
{
  "total": 3,
  "list": [
    {
      "id": 1,
      "issueId": 1,
      "action": "create",
      "processorId": 2,
      "processorName": "李四",
      "processTime": "2025-06-01 09:30:00",
      "content": "发现空调运行异常，温度波动较大",
      "createTime": "2025-06-01 09:30:00"
    }
  ]
}
```

**修改后**:
```json
{
  "total": 3,
  "list": [
    {
      "id": 1,
      "issueId": 1,
      "action": "create",
      "processorId": 2,
      "processorName": "李四",
      "processTime": "2025-06-01 09:30:00",
      "content": "发现空调运行异常，温度波动较大",
      "images": [
        "/data/uploads/2025/06/issue1_1.jpg",
        "/data/uploads/2025/06/issue1_2.jpg"
      ],
      "createTime": "2025-06-01 09:30:00"
    }
  ]
}
```

#### 处理问题接口
**路径**: `POST /api/v1/issues/{id}/process`  
**分类**: [COMMON]  
**修改内容**: 请求参数增加`images`字段  
**修改前**:
```json
{
  "action": "process",
  "content": "检查空调系统，发现滤网堵塞"
}
```

**修改后**:
```json
{
  "action": "process",
  "content": "检查空调系统，发现滤网堵塞",
  "images": [
    "/data/uploads/2025/06/issue1_3.jpg"
  ]
}
```

## 3. 新增接口

### 3.1 用户头像相关接口

#### 上传用户头像
**路径**: `POST /api/v1/users/{id}/avatar`  
**分类**: [COMMON]  
**请求类型**: `multipart/form-data`  
**请求参数**:
- `file`: 图片文件

**响应**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "avatarUrl": "https://api.example.com/avatars/default-1.png"
  }
}
```

#### 获取用户头像
**路径**: `GET /api/v1/users/{id}/avatar`  
**分类**: [COMMON]  
**响应**: 直接返回图片文件

### 3.2 区域二维码相关接口

#### 获取区域二维码
**路径**: `GET /api/v1/areas/{id}/qrcode`  
**分类**: [COMMON]  
**响应**: 直接返回二维码图片

#### 生成区域二维码
**路径**: `POST /api/v1/areas/{id}/qrcode`  
**分类**: [ADMIN]  
**响应**:
```json
{
  "code": 200,
  "message": "生成成功",
  "data": {
    "qrCodeUrl": "https://api.example.com/qrcode/area/1"
  }
}
```

### 3.3 巡检路径相关接口

#### 提交巡检路径
**路径**: `POST /api/v1/records/{id}/route`  
**分类**: [APP]  
**请求参数**:
```json
{
  "routePath": [
    {"timestamp": "2025-06-01 09:00:00", "latitude": 30.65984, "longitude": 104.06538, "location": "入口"},
    {"timestamp": "2025-06-01 09:05:00", "latitude": 30.65990, "longitude": 104.06545, "location": "设备区1"},
    {"timestamp": "2025-06-01 09:10:00", "latitude": 30.66000, "longitude": 104.06550, "location": "设备区2"},
    {"timestamp": "2025-06-01 10:30:00", "latitude": 30.65984, "longitude": 104.06538, "location": "出口"}
  ]
}
```

**响应**:
```json
{
  "code": 200,
  "message": "提交成功",
  "data": null
}
```

#### 获取巡检路径
**路径**: `GET /api/v1/records/{id}/route`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "routePath": [
      {"timestamp": "2025-06-01 09:00:00", "latitude": 30.65984, "longitude": 104.06538, "location": "入口"},
      {"timestamp": "2025-06-01 09:05:00", "latitude": 30.65990, "longitude": 104.06545, "location": "设备区1"},
      {"timestamp": "2025-06-01 09:10:00", "latitude": 30.66000, "longitude": 104.06550, "location": "设备区2"},
      {"timestamp": "2025-06-01 10:30:00", "latitude": 30.65984, "longitude": 104.06538, "location": "出口"}
    ]
  }
}
```

### 3.4 问题处理图片相关接口

#### 上传问题处理图片
**路径**: `POST /api/v1/issue-processes/{id}/images`  
**分类**: [COMMON]  
**请求类型**: `multipart/form-data`  
**请求参数**:
- `files`: 图片文件（可多个）

**响应**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "images": [
      "/data/uploads/2025/06/issue1_1.jpg",
      "/data/uploads/2025/06/issue1_2.jpg"
    ]
  }
}
```

#### 获取问题处理图片
**路径**: `GET /api/v1/issue-processes/{id}/images`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "images": [
      "/data/uploads/2025/06/issue1_1.jpg",
      "/data/uploads/2025/06/issue1_2.jpg"
    ]
  }
}
```

#### 删除问题处理图片
**路径**: `DELETE /api/v1/issue-processes/{id}/images/{imageId}`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 3.5 文件上传管理接口

#### 上传文件
**路径**: `POST /api/v1/files/upload`  
**分类**: [COMMON]  
**请求类型**: `multipart/form-data`  
**请求参数**:
- `file`: 文件
- `businessType`: 业务类型（可选）
- `businessId`: 业务ID（可选）

**响应**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": 1,
    "fileName": "巡检手册.pdf",
    "filePath": "/data/uploads/2025/05/manual.pdf",
    "fileSize": 2458621,
    "fileType": "application/pdf",
    "uploadUserId": 1,
    "businessType": "SYSTEM",
    "businessId": null,
    "createTime": "2025-05-15 10:30:00"
  }
}
```

#### 获取文件列表
**路径**: `GET /api/v1/files/list`  
**分类**: [COMMON]  
**请求参数**:
- `businessType`: 业务类型（必选）
- `businessId`: 业务ID（可选）

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "fileName": "巡检手册.pdf",
      "filePath": "/data/uploads/2025/05/manual.pdf",
      "fileSize": 2458621,
      "fileType": "application/pdf",
      "uploadUserId": 1,
      "businessType": "SYSTEM",
      "businessId": null,
      "createTime": "2025-05-15 10:30:00"
    }
  ]
}
```

#### 删除文件
**路径**: `DELETE /api/v1/files/{id}`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 3.6 系统参数管理接口

#### 获取所有系统参数
**路径**: `GET /api/v1/system/params`  
**分类**: [ADMIN]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "system.name": "巡检管理系统",
    "system.version": "v1.0.7",
    "task.auto_assign": "true",
    "task.reminder_minutes": "30",
    "notification.enable": "true",
    "file.upload.path": "/data/uploads",
    "file.allowed_types": "jpg,jpeg,png,pdf,doc,docx",
    "file.max_size": "10485760"
  }
}
```

#### 获取指定系统参数
**路径**: `GET /api/v1/system/params/{key}`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": "巡检管理系统"
}
```

#### 设置系统参数
**路径**: `POST /api/v1/system/params`  
**分类**: [ADMIN]  
**请求参数**:
```json
{
  "paramKey": "system.name",
  "paramValue": "巡检管理系统",
  "paramDesc": "系统名称"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "设置成功",
  "data": true
}
```

### 3.7 统计数据接口

#### 获取每日巡检统计
**路径**: `GET /api/v1/statistics/inspection/daily`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "date": "2025-06-01",
    "total": 6,
    "normal": 5,
    "abnormal": 1,
    "by_area": [
      {"area_id": 1, "area_name": "主机房A区", "count": 2},
      {"area_id": 2, "area_name": "配电室B区", "count": 2},
      {"area_id": 3, "area_name": "网络机房C区", "count": 2}
    ]
  }
}
```

#### 获取每周巡检统计
**路径**: `GET /api/v1/statistics/inspection/weekly`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "start_date": "2025-05-26",
    "end_date": "2025-06-01",
    "total": 24,
    "normal": 20,
    "abnormal": 4,
    "by_day": [
      {"date": "2025-05-26", "count": 3},
      {"date": "2025-05-27", "count": 4},
      {"date": "2025-05-28", "count": 3},
      {"date": "2025-05-29", "count": 5},
      {"date": "2025-05-30", "count": 3},
      {"date": "2025-05-31", "count": 3},
      {"date": "2025-06-01", "count": 3}
    ]
  }
}
```

#### 获取问题统计
**路径**: `GET /api/v1/statistics/issues`  
**分类**: [COMMON]  
**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "open": 3,
    "processing": 4,
    "closed": 3,
    "by_type": [
      {"type": "environment", "count": 4},
      {"type": "security", "count": 3},
      {"type": "device", "count": 2},
      {"type": "other", "count": 1}
    ]
  }
}
```

## 4. 实现计划

1. 先更新实体类、DTO和Mapper接口（部分已完成）
2. 实现Service层接口和方法
3. 实现Controller层接口
4. 更新现有接口文档
5. 编写单元测试和集成测试
6. 与前端联调测试 