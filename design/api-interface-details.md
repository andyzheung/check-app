# 巡检系统API接口详细文档

## 概述

本文档详细说明巡检系统API接口的请求和响应格式，所有接口均遵循统一的响应格式。接口按功能模块划分，并根据`[APP]`、`[ADMIN]`和`[COMMON]`标识区分不同用途。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

### 全局状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 1. 文件上传相关接口

### 1.1 上传文件 [COMMON]

**接口路径**: `POST /api/v1/files/upload`

**请求类型**: `multipart/form-data`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| file | File | 是 | 上传的文件 |
| businessType | String | 否 | 业务类型，如：USER、AREA、RECORD、ISSUE等 |
| businessId | Long | 否 | 业务ID |

**响应示例**:

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

### 1.2 获取文件列表 [COMMON]

**接口路径**: `GET /api/v1/files/list`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| businessType | String | 是 | 业务类型 |
| businessId | Long | 否 | 业务ID |

**响应示例**:

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
    },
    {
      "id": 2,
      "fileName": "设备说明书.pdf",
      "filePath": "/data/uploads/2025/05/equipment.pdf",
      "fileSize": 1536842,
      "fileType": "application/pdf",
      "uploadUserId": 1,
      "businessType": "SYSTEM",
      "businessId": null,
      "createTime": "2025-05-15 11:15:00"
    }
  ]
}
```

### 1.3 删除文件 [COMMON]

**接口路径**: `DELETE /api/v1/files/{id}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 文件ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 1.4 获取文件信息 [COMMON]

**接口路径**: `GET /api/v1/files/{id}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 文件ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
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

## 2. 系统参数相关接口

### 2.1 获取所有系统参数 [ADMIN]

**接口路径**: `GET /api/v1/system/params`

**请求参数**: 无

**响应示例**:

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

### 2.2 获取指定系统参数 [COMMON]

**接口路径**: `GET /api/v1/system/params/{key}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| key | String | 是 | 参数键 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": "巡检管理系统"
}
```

### 2.3 设置系统参数 [ADMIN]

**接口路径**: `POST /api/v1/system/params`

**请求参数**:

```json
{
  "paramKey": "system.name",
  "paramValue": "巡检管理系统",
  "paramDesc": "系统名称"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "设置成功",
  "data": true
}
```

### 2.4 删除系统参数 [ADMIN]

**接口路径**: `DELETE /api/v1/system/params/{key}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| key | String | 是 | 参数键 |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

## 3. 统计数据相关接口

### 3.1 获取每日巡检统计 [COMMON]

**接口路径**: `GET /api/v1/statistics/inspection/daily`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| date | String | 否 | 日期，格式：yyyy-MM-dd，不传则默认为当天 |

**响应示例**:

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

### 3.2 获取每周巡检统计 [COMMON]

**接口路径**: `GET /api/v1/statistics/inspection/weekly`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| startDate | String | 否 | 开始日期，格式：yyyy-MM-dd，不传则默认为本周一 |
| endDate | String | 否 | 结束日期，格式：yyyy-MM-dd，不传则默认为本周日 |

**响应示例**:

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

### 3.3 获取问题统计 [COMMON]

**接口路径**: `GET /api/v1/statistics/issues`

**请求参数**: 无

**响应示例**:

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

### 3.4 获取仪表盘数据 [ADMIN]

**接口路径**: `GET /api/v1/statistics/dashboard`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "tasks": {
      "total": 56,
      "pending": 10,
      "inProgress": 5,
      "completed": 41
    },
    "records": {
      "total": 120,
      "thisMonth": 24,
      "lastMonth": 32
    },
    "issues": {
      "total": 25,
      "open": 5,
      "processing": 8,
      "closed": 12
    },
    "areas": {
      "total": 12,
      "active": 10,
      "inactive": 2
    },
    "users": {
      "total": 35,
      "active": 30,
      "inactive": 5
    }
  }
}
```

### 3.5 刷新统计缓存 [ADMIN]

**接口路径**: `POST /api/v1/statistics/refresh/{type}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| type | String | 是 | 缓存类型，可选值：daily, weekly, issues, dashboard, all |

**响应示例**:

```json
{
  "code": 200,
  "message": "刷新成功",
  "data": true
}
```

## 4. 用户相关接口

### 4.1 获取用户详情 [COMMON]

**接口路径**: `GET /api/v1/users/{id}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
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
}
```

### 4.2 获取当前用户信息 [COMMON]

**接口路径**: `GET /api/v1/auth/info`

**请求头**:
- Authorization: Bearer {token}

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "系统管理员",
    "departmentId": 1,
    "departmentName": "IT部门",
    "role": "admin",
    "avatar": "https://api.example.com/avatars/default-1.png",
    "permissions": ["dashboard", "records_view", "records_all", "records_export", "issues_view", "issues_edit", "user_manage", "system_config"]
  }
}
```

### 4.3 上传用户头像 [COMMON]

**接口路径**: `POST /api/v1/users/{id}/avatar`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 用户ID |

**请求类型**: `multipart/form-data`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| file | File | 是 | 图片文件 |

**响应示例**:

```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "avatarUrl": "https://api.example.com/avatars/default-1.png"
  }
}
```

### 4.4 获取用户头像 [COMMON]

**接口路径**: `GET /api/v1/users/{id}/avatar`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 用户ID |

**响应**: 直接返回图片文件

## 5. 巡检区域相关接口

### 5.1 获取区域详情 [COMMON]

**接口路径**: `GET /api/v1/areas/{id}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 区域ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
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
}
```

### 5.2 获取区域列表 [COMMON]

**接口路径**: `GET /api/v1/areas`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| type | String | 否 | 区域类型，如：server、power、network等 |
| status | String | 否 | 状态，如：active、inactive |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 3,
    "list": [
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
      },
      {
        "id": 2,
        "code": "AREA102",
        "name": "配电室B区",
        "description": "电力设备配电间",
        "address": "数据中心地下1层",
        "type": "power",
        "status": "active",
        "qrCodeUrl": "https://api.example.com/qrcode/area/2",
        "createTime": "2025-01-01 00:00:00"
      },
      {
        "id": 3,
        "code": "AREA103",
        "name": "网络机房C区",
        "description": "网络设备机房",
        "address": "数据中心2楼",
        "type": "network",
        "status": "active",
        "qrCodeUrl": "https://api.example.com/qrcode/area/3",
        "createTime": "2025-01-01 00:00:00"
      }
    ],
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

### 5.3 获取区域二维码 [COMMON]

**接口路径**: `GET /api/v1/areas/{id}/qrcode`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 区域ID |

**响应**: 直接返回二维码图片

### 5.4 生成区域二维码 [ADMIN]

**接口路径**: `POST /api/v1/areas/{id}/qrcode`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 区域ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "生成成功",
  "data": {
    "qrCodeUrl": "https://api.example.com/qrcode/area/1"
  }
}
```

## 6. 巡检记录相关接口

### 6.1 获取巡检记录详情 [COMMON]

**接口路径**: `GET /api/v1/records/{id}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 巡检记录ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
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
}
```

### 6.2 获取巡检记录列表 [COMMON]

**接口路径**: `GET /api/v1/records`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| areaId | Long | 否 | 区域ID |
| inspectorId | Long | 否 | 巡检人员ID |
| status | String | 否 | 状态，如：pending、in_progress、completed |
| startDate | String | 否 | 开始日期，格式：yyyy-MM-dd |
| endDate | String | 否 | 结束日期，格式：yyyy-MM-dd |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 2,
    "list": [
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
      },
      {
        "id": 2,
        "recordNo": "REC20250601002",
        "taskId": 2,
        "areaId": 2,
        "areaName": "配电室B区",
        "inspectorId": 2,
        "inspectorName": "李四",
        "startTime": "2025-06-01 14:00:00",
        "endTime": "2025-06-01 15:30:00",
        "status": "completed",
        "remark": "正常巡检，设备运行良好",
        "createTime": "2025-06-01 14:00:00"
      }
    ],
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

### 6.3 提交巡检路径 [APP]

**接口路径**: `POST /api/v1/records/{id}/route`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 巡检记录ID |

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

**响应示例**:

```json
{
  "code": 200,
  "message": "提交成功",
  "data": null
}
```

### 6.4 获取巡检路径 [COMMON]

**接口路径**: `GET /api/v1/records/{id}/route`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 巡检记录ID |

**响应示例**:

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

## 7. 问题处理图片相关接口

### 7.1 上传问题处理图片 [COMMON]

**接口路径**: `POST /api/v1/issue-processes/{id}/images`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 问题处理记录ID |

**请求类型**: `multipart/form-data`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| files | File[] | 是 | 图片文件（可多个） |

**响应示例**:

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

### 7.2 获取问题处理图片 [COMMON]

**接口路径**: `GET /api/v1/issue-processes/{id}/images`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 问题处理记录ID |

**响应示例**:

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

### 7.3 删除问题处理图片 [COMMON]

**接口路径**: `DELETE /api/v1/issue-processes/{id}/images/{imageId}`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 问题处理记录ID |
| imageId | Long | 是 | 图片ID（文件上传表中的ID） |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
``` 