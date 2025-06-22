# 巡检系统接口设计文档

## 1. 概述

本文档定义了巡检系统前后端接口设计，基于现有架构进行扩展设计，确保与现有系统兼容并支持后续功能扩展。

### 1.1 设计原则

- **兼容性**: 与现有系统架构保持兼容
- **可扩展性**: 支持未来功能扩展
- **一致性**: 接口格式和命名保持一致
- **安全性**: 遵循安全最佳实践
- **最小修改**: 尽可能复用现有数据库表结构

### 1.2 基础URL

```
/api/v1
```

### 1.3 全局响应格式

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据
}
```

### 1.4 通用状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 用户认证接口

### 2.1 用户登录

**接口**: `POST /auth/login`

**请求参数**:

```json
{
  "username": "admin",
  "password": "password"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "role": "admin",
      "permissions": ["dashboard", "records_view", "records_all", "records_export", "issues_view", "issues_edit", "user_manage", "system_config"]
    }
  }
}
```

### 2.2 获取用户信息

**接口**: `GET /auth/info`

**请求头**:
- Authorization: Bearer {token}

**响应**:

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
    "permissions": ["dashboard", "records_view", "records_all", "records_export", "issues_view", "issues_edit", "user_manage", "system_config"]
  }
}
```

### 2.3 退出登录

**接口**: `POST /auth/logout`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

## 3. 用户管理接口

### 3.1 获取用户列表

**接口**: `GET /users`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- username: 用户名（可选，模糊查询）
- realName: 真实姓名（可选，模糊查询）
- departmentId: 部门ID（可选）
- role: 角色（可选）
- status: 状态（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
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
    ]
  }
}
```

### 3.2 获取用户详情

**接口**: `GET /users/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

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
    "avatar": "https://example.com/avatar.jpg",
    "lastLoginTime": "2025-06-10 10:00:00",
    "createTime": "2025-01-01 00:00:00"
  }
}
```

### 3.3 创建用户

**接口**: `POST /users`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "username": "zhangsan",
  "password": "password",
  "realName": "张三",
  "departmentId": 2,
  "role": "user",
  "phone": "13900139000",
  "email": "zhangsan@example.com"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 6
  }
}
```

### 3.4 更新用户

**接口**: `PUT /users/{id}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "realName": "张三（更新）",
  "departmentId": 3,
  "role": "admin",
  "status": "active",
  "phone": "13900139001",
  "email": "zhangsan_new@example.com"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 3.5 删除用户

**接口**: `DELETE /users/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 3.6 修改密码

**接口**: `PUT /users/{id}/password`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "oldPassword": "old_password",
  "newPassword": "new_password"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

### 3.7 获取用户权限

**接口**: `GET /users/{id}/permissions`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "code": "dashboard",
      "name": "仪表盘查看权限"
    },
    {
      "code": "records_view",
      "name": "巡检记录查看权限"
    }
  ]
}
```

### 3.8 更新用户权限

**接口**: `PUT /users/{id}/permissions`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "permissions": ["dashboard", "records_view", "issues_view"]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "权限更新成功",
  "data": null
}
```

## 4. 区域管理接口

### 4.1 获取区域列表

**接口**: `GET /areas`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- name: 区域名称（可选，模糊查询）
- type: 区域类型（可选）
- status: 状态（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "code": "AREA001",
        "areaCode": "AREAA001",
        "name": "机房A",
        "description": "主机房",
        "type": "A",
        "typeName": "机房",
        "address": "数据中心1层",
        "status": "active",
        "qrCodeUrl": "https://example.com/qrcode/area001.png",
        "createTime": "2025-01-01 00:00:00"
      }
    ]
  }
}
```

### 4.2 获取区域详情

**接口**: `GET /areas/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "code": "AREA001",
    "areaCode": "AREAA001",
    "name": "机房A",
    "description": "主机房",
    "type": "A",
    "typeName": "机房",
    "address": "数据中心1层",
    "status": "active",
    "qrCodeUrl": "https://example.com/qrcode/area001.png",
    "createTime": "2025-01-01 00:00:00",
    "checkItems": [
      {
        "id": 1,
        "name": "温度",
        "type": "environment",
        "required": true,
        "sort": 1
      },
      {
        "id": 2,
        "name": "湿度",
        "type": "environment",
        "required": true,
        "sort": 2
      }
    ]
  }
}
```

### 4.3 创建区域

**接口**: `POST /areas`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "code": "AREA009",
  "name": "新机房",
  "description": "新增机房",
  "type": "A",
  "address": "数据中心2层",
  "checkItems": [
    {
      "name": "温度",
      "type": "environment",
      "required": true,
      "sort": 1
    },
    {
      "name": "湿度",
      "type": "environment",
      "required": true,
      "sort": 2
    }
  ]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 9,
    "areaCode": "AREAA009"
  }
}
```

### 4.4 更新区域

**接口**: `PUT /areas/{id}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "name": "机房A（更新）",
  "description": "更新后的主机房",
  "address": "数据中心1层A区",
  "status": "active"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

## 5. 巡检任务接口

### 5.1 获取巡检任务列表

**接口**: `GET /tasks`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
**响应**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 4.6 获取区域二维码

**接口**: `GET /areas/{id}/qrcode`

**请求头**:
- Authorization: Bearer {token}

**响应**: 
二维码图片文件（image/png）

### 4.7 获取区域类型列表

**接口**: `GET /area-types`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "typeCode": "A",
      "typeName": "机房",
      "description": "各类机房区域"
    },
    {
      "typeCode": "B",
      "typeName": "办公区",
      "description": "办公室、会议室等区域"
    },
    {
      "typeCode": "C",
      "typeName": "设备区",
      "description": "设备安装、维护区域"
    }
  ]
}
```

### 4.8 更新区域巡检项

**接口**: `PUT /areas/{id}/check-items`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "checkItems": [
    {
      "id": 1,
      "name": "温度",
      "type": "environment",
      "required": true,
      "sort": 1
    },
    {
      "name": "电压",
      "type": "environment",
      "required": true,
      "sort": 3
    }
  ]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

## 5. 巡检任务接口

### 5.1 获取巡检任务列表

**接口**: `GET /inspection-tasks`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- taskName: 任务名称（可选，模糊查询）
- areaId: 区域ID（可选）
- inspectorId: 巡检人员ID（可选）
- status: 状态（可选）
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "taskName": "机房A区日常巡检",
        "areaId": 1,
        "areaName": "机房A",
        "inspectorId": 1,
        "inspectorName": "张三",
        "planTime": "2025-06-10 10:00:00",
        "status": "pending",
        "createUserId": 1,
        "createUserName": "管理员",
        "createTime": "2025-06-09 10:00:00"
      }
    ]
  }
}
```

### 5.2 获取巡检任务详情

**接口**: `GET /inspection-tasks/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "taskName": "机房A区日常巡检",
    "areaId": 1,
    "areaName": "机房A",
    "areaCode": "AREAA001",
    "inspectorId": 1,
    "inspectorName": "张三",
    "planTime": "2025-06-10 10:00:00",
    "status": "pending",
    "createUserId": 1,
    "createUserName": "管理员",
    "createTime": "2025-06-09 10:00:00",
    "record": {
      "id": 5,
      "recordNo": "R20250610100523",
      "startTime": "2025-06-10 10:05:23",
      "endTime": "2025-06-10 10:30:45",
      "status": "normal"
    }
  }
}
```

### 5.3 创建巡检任务

**接口**: `POST /inspection-tasks`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "taskName": "机房B区日常巡检",
  "areaId": 2,
  "inspectorId": 2,
  "planTime": "2025-06-11 10:00:00"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 10
  }
}
```

### 5.4 更新巡检任务

**接口**: `PUT /inspection-tasks/{id}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "taskName": "机房B区日常巡检（更新）",
  "inspectorId": 3,
  "planTime": "2025-06-11 14:00:00",
  "status": "pending"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 5.5 删除巡检任务

**接口**: `DELETE /inspection-tasks/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 5.6 取消巡检任务

**接口**: `PUT /inspection-tasks/{id}/cancel`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "reason": "设备维护，暂停巡检"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "任务已取消",
  "data": null
}
```

### 5.7 批量创建巡检任务

**接口**: `POST /inspection-tasks/batch`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "tasks": [
    {
      "taskName": "机房A区日常巡检",
      "areaId": 1,
      "inspectorId": 1,
      "planTime": "2025-06-12 10:00:00"
    },
    {
      "taskName": "机房B区日常巡检",
      "areaId": 2,
      "inspectorId": 2,
      "planTime": "2025-06-12 14:00:00"
    }
  ]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "批量创建成功",
  "data": {
    "successCount": 2,
    "ids": [11, 12]
  }
}
```

## 6. 巡检记录接口

### 6.1 获取巡检记录列表

**接口**: `GET /inspection-records`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- recordNo: 记录编号（可选）
- areaId: 区域ID（可选）
- inspectorId: 巡检人员ID（可选）
- status: 状态（可选）
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "recordNo": "R20250610100523",
        "taskId": 1,
        "taskName": "机房A区日常巡检",
        "areaId": 1,
        "areaName": "机房A",
        "inspectorId": 1,
        "inspectorName": "张三",
        "startTime": "2025-06-10 10:05:23",
        "endTime": "2025-06-10 10:30:45",
        "status": "normal",
        "remark": "",
        "createTime": "2025-06-10 10:05:23"
      }
    ]
  }
}
```

### 6.2 获取巡检记录详情

**接口**: `GET /inspection-records/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "recordNo": "R20250610100523",
    "taskId": 1,
    "taskName": "机房A区日常巡检",
    "areaId": 1,
    "areaName": "机房A",
    "areaCode": "AREAA001",
    "inspectorId": 1,
    "inspectorName": "张三",
    "startTime": "2025-06-10 10:05:23",
    "endTime": "2025-06-10 10:30:45",
    "status": "normal",
    "remark": "",
    "routePath": null,
    "createTime": "2025-06-10 10:05:23",
    "items": [
      {
        "id": 1,
        "name": "温度",
        "type": "environment",
        "status": "normal",
        "remark": "温度正常，22°C"
      },
      {
        "id": 2,
        "name": "湿度",
        "type": "environment",
        "status": "normal",
        "remark": "湿度正常，45%"
      }
    ]
  }
}
```

### 6.3 创建巡检记录

**接口**: `POST /inspection-records`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "taskId": 2,
  "areaId": 2,
  "startTime": "2025-06-10 14:05:00",
  "items": [
    {
      "name": "温度",
      "type": "environment",
      "status": "normal",
      "remark": "温度正常，23°C"
    },
    {
      "name": "湿度",
      "type": "environment",
      "status": "abnormal",
      "remark": "湿度异常，65%，超过标准范围"
    }
  ],
  "remark": "巡检过程中发现湿度异常"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 15,
    "recordNo": "R20250610140500"
  }
}
```

### 6.4 更新巡检记录

**接口**: `PUT /inspection-records/{id}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "endTime": "2025-06-10 14:30:00",
  "status": "abnormal",
  "remark": "巡检过程中发现湿度异常，已通知维护人员"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 6.5 删除巡检记录

**接口**: `DELETE /inspection-records/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 6.6 更新巡检项状态

**接口**: `PUT /inspection-records/{recordId}/items/{itemId}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "status": "abnormal",
  "remark": "温度异常，28°C，超过标准范围"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 6.7 批量创建巡检项

**接口**: `POST /inspection-records/{id}/items/batch`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "items": [
    {
      "name": "电压",
      "type": "environment",
      "status": "normal",
      "remark": "电压正常，220V"
    },
    {
      "name": "电流",
      "type": "environment",
      "status": "normal",
      "remark": "电流正常，5A"
    }
  ]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "批量创建成功",
  "data": {
    "successCount": 2,
    "ids": [21, 22]
  }
}
```

### 6.8 导出巡检记录

**接口**: `GET /inspection-records/export`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- recordNo: 记录编号（可选）
- areaId: 区域ID（可选）
- inspectorId: 巡检人员ID（可选）
- status: 状态（可选）
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）
- exportType: 导出类型，excel或pdf，默认excel

**响应**: 
文件下载（application/vnd.ms-excel 或 application/pdf）

## 7. 问题管理接口

### 7.1 获取问题列表

**接口**: `GET /issues`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- issueNo: 问题编号（可选）
- recordId: 巡检记录ID（可选）
- type: 问题类型（可选）
- status: 状态（可选）
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "issueNo": "IS20250610001",
        "recordId": 10,
        "recordNo": "R20250610140500",
        "areaId": 2,
        "areaName": "机房B",
        "description": "湿度异常，65%，超过标准范围",
        "type": "environment",
        "status": "open",
        "recorderId": 2,
        "recorderName": "李四",
        "handlerId": null,
        "handlerName": null,
        "handleTime": null,
        "createTime": "2025-06-10 14:30:00"
      }
    ]
  }
}
```

### 7.2 获取问题详情

**接口**: `GET /issues/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "issueNo": "IS20250610001",
    "recordId": 10,
    "recordNo": "R20250610140500",
    "itemId": 15,
    "itemName": "湿度",
    "areaId": 2,
    "areaName": "机房B",
    "description": "湿度异常，65%，超过标准范围",
    "type": "environment",
    "status": "open",
    "recorderId": 2,
    "recorderName": "李四",
    "handlerId": null,
    "handlerName": null,
    "handleTime": null,
    "handleResult": null,
    "createTime": "2025-06-10 14:30:00",
    "processes": []
  }
}
```

### 7.3 创建问题

**接口**: `POST /issues`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "recordId": 11,
  "itemId": 18,
  "description": "温度异常，28°C，超过标准范围",
  "type": "environment"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 2,
    "issueNo": "IS20250610002"
  }
}
```

### 7.4 更新问题

**接口**: `PUT /issues/{id}`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "description": "温度异常，29°C，超过标准范围",
  "type": "environment",
  "status": "processing"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 7.5 删除问题

**接口**: `DELETE /issues/{id}`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 7.6 处理问题

**接口**: `POST /issues/{id}/process`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "action": "process",
  "content": "已联系维修人员，正在处理中",
  "images": ["https://example.com/image1.jpg", "https://example.com/image2.jpg"]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "处理成功",
  "data": {
    "id": 1
  }
}
```

### 7.7 关闭问题

**接口**: `POST /issues/{id}/close`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "handleResult": "已修复湿度控制系统，当前湿度已恢复正常",
  "images": ["https://example.com/image3.jpg"]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "问题已关闭",
  "data": null
}
```

### 7.8 获取问题处理记录

**接口**: `GET /issues/{id}/processes`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "action": "create",
      "processorId": 2,
      "processorName": "李四",
      "processTime": "2025-06-10 14:30:00",
      "content": "发现湿度异常，65%，超过标准范围",
      "images": []
    },
    {
      "id": 2,
      "action": "process",
      "processorId": 3,
      "processorName": "王五",
      "processTime": "2025-06-10 15:30:00",
      "content": "已联系维修人员，正在处理中",
      "images": ["https://example.com/image1.jpg", "https://example.com/image2.jpg"]
    }
  ]
}
```

### 7.9 上传问题相关图片

**接口**: `POST /issues/upload`

**请求头**:
- Authorization: Bearer {token}
- Content-Type: multipart/form-data

**请求参数**:
- file: 文件对象

**响应**:

```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://example.com/uploads/image123.jpg"
  }
}
```

## 8. 统计分析接口

### 8.1 获取仪表盘数据

**接口**: `GET /statistics/dashboard`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalInspections": 1280,
    "totalInspectors": 256,
    "totalIssues": 80,
    "activeInspectors": 48,
    "weeklyInspections": 128,
    "weeklyInspectorsCount": 24,
    "weeklyIssuesCount": 8,
    "weeklyActiveInspectors": 16,
    "inspectionTrend": [
      {"date": "2025-06-04", "count": 25},
      {"date": "2025-06-05", "count": 28},
      {"date": "2025-06-06", "count": 22},
      {"date": "2025-06-07", "count": 15},
      {"date": "2025-06-08", "count": 10},
      {"date": "2025-06-09", "count": 30},
      {"date": "2025-06-10", "count": 35}
    ],
    "issuesTrend": [
      {"date": "2025-06-04", "count": 2},
      {"date": "2025-06-05", "count": 1},
      {"date": "2025-06-06", "count": 0},
      {"date": "2025-06-07", "count": 1},
      {"date": "2025-06-08", "count": 0},
      {"date": "2025-06-09", "count": 3},
      {"date": "2025-06-10", "count": 2}
    ],
    "areaDistribution": [
      {"name": "机房A", "count": 45},
      {"name": "机房B", "count": 38},
      {"name": "机房C", "count": 25},
      {"name": "配电室", "count": 12},
      {"name": "监控室", "count": 8}
    ],
    "issueTypeDistribution": [
      {"type": "environment", "count": 35},
      {"type": "security", "count": 22},
      {"type": "device", "count": 18},
      {"type": "other", "count": 5}
    ]
  }
}
```

### 8.2 获取巡检统计数据

**接口**: `GET /statistics/inspections`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）
- areaId: 区域ID（可选）
- inspectorId: 巡检人员ID（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalCount": 150,
    "normalCount": 135,
    "abnormalCount": 15,
    "areaDistribution": [
      {"name": "机房A", "count": 45, "normalCount": 42, "abnormalCount": 3},
      {"name": "机房B", "count": 38, "normalCount": 35, "abnormalCount": 3},
      {"name": "机房C", "count": 25, "normalCount": 23, "abnormalCount": 2},
      {"name": "配电室", "count": 12, "normalCount": 10, "abnormalCount": 2},
      {"name": "监控室", "count": 8, "normalCount": 7, "abnormalCount": 1}
    ],
    "timeTrend": [
      {"date": "2025-06-04", "count": 25, "normalCount": 24, "abnormalCount": 1},
      {"date": "2025-06-05", "count": 28, "normalCount": 26, "abnormalCount": 2},
      {"date": "2025-06-06", "count": 22, "normalCount": 20, "abnormalCount": 2},
      {"date": "2025-06-07", "count": 15, "normalCount": 14, "abnormalCount": 1},
      {"date": "2025-06-08", "count": 10, "normalCount": 9, "abnormalCount": 1},
      {"date": "2025-06-09", "count": 30, "normalCount": 27, "abnormalCount": 3},
      {"date": "2025-06-10", "count": 35, "normalCount": 32, "abnormalCount": 3}
    ],
    "inspectorRanking": [
      {"name": "张三", "count": 42},
      {"name": "李四", "count": 35},
      {"name": "王五", "count": 28},
      {"name": "赵六", "count": 15}
    ]
  }
}
```

### 8.3 获取问题统计数据

**接口**: `GET /statistics/issues`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）
- areaId: 区域ID（可选）
- type: 问题类型（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalCount": 80,
    "openCount": 20,
    "processingCount": 25,
    "closedCount": 35,
    "typeDistribution": [
      {"type": "environment", "typeName": "环境问题", "count": 35},
      {"type": "security", "typeName": "安全问题", "count": 22},
      {"type": "device", "typeName": "设备问题", "count": 18},
      {"type": "other", "typeName": "其他", "count": 5}
    ],
    "statusDistribution": [
      {"status": "open", "statusName": "未处理", "count": 20},
      {"status": "processing", "statusName": "处理中", "count": 25},
      {"status": "closed", "statusName": "已关闭", "count": 35}
    ],
    "areaDistribution": [
      {"name": "机房A", "count": 25},
      {"name": "机房B", "count": 20},
      {"name": "机房C", "count": 15},
      {"name": "配电室", "count": 12},
      {"name": "监控室", "count": 8}
    ],
    "timeTrend": [
      {"date": "2025-06-04", "count": 8},
      {"date": "2025-06-05", "count": 12},
      {"date": "2025-06-06", "count": 10},
      {"date": "2025-06-07", "count": 5},
      {"date": "2025-06-08", "count": 7},
      {"date": "2025-06-09", "count": 18},
      {"date": "2025-06-10", "count": 20}
    ]
  }
}
```

## 9. 系统管理接口

### 9.1 获取部门列表

**接口**: `GET /departments`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "IT部门",
      "code": "it",
      "parentId": null,
      "sort": 1,
      "status": 1
    },
    {
      "id": 2,
      "name": "运维部门",
      "code": "ops",
      "parentId": null,
      "sort": 2,
      "status": 1
    },
    {
      "id": 3,
      "name": "安防部门",
      "code": "security",
      "parentId": null,
      "sort": 3,
      "status": 1
    }
  ]
}
```

### 9.2 获取系统消息

**接口**: `GET /notifications`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- type: 消息类型（可选）
- status: 状态（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 8,
    "list": [
      {
        "id": 1,
        "title": "新任务通知",
        "content": "您有一个新的巡检任务待处理",
        "type": "task",
        "status": "unread",
        "createTime": "2025-06-09 20:18:45"
      },
      {
        "id": 2,
        "title": "系统维护通知",
        "content": "系统将于今晚22:00进行维护升级",
        "type": "system",
        "status": "unread",
        "createTime": "2025-06-09 20:18:45"
      }
    ]
  }
}
```

### 9.3 标记消息为已读

**接口**: `PUT /notifications/{id}/read`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "标记成功",
  "data": null
}
```

### 9.4 批量标记消息为已读

**接口**: `PUT /notifications/read-batch`

**请求头**:
- Authorization: Bearer {token}

**请求参数**:

```json
{
  "ids": [1, 2, 3]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "批量标记成功",
  "data": null
}
```

### 9.5 获取系统参数

**接口**: `GET /system/params`

**请求头**:
- Authorization: Bearer {token}

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "systemName": "巡检管理系统",
    "systemLogo": "https://example.com/logo.png",
    "systemVersion": "1.0.0",
    "defaultPageSize": 10,
    "fileUploadLimit": 10,
    "allowedFileTypes": "jpg,jpeg,png,pdf",
    "maxFileSize": 5
  }
}
```

## 10. 数据库设计说明

本系统接口设计基于现有数据库结构，主要使用以下数据表：

1. **t_user** - 用户表：存储系统用户信息
2. **t_user_permission** - 用户权限表：存储用户权限信息
3. **t_department** - 部门表：存储部门信息
4. **t_area** - 区域表：存储巡检区域信息
5. **t_area_type** - 区域类型表：存储区域类型信息
6. **t_area_check_item** - 区域巡检项模板表：存储区域巡检项信息
7. **t_inspection_task** - 巡检任务表：存储巡检任务信息
8. **t_inspection_record** - 巡检记录表：存储巡检记录信息
9. **t_inspection_item** - 巡检项表：存储巡检项信息
10. **t_issue** - 问题表：存储问题信息
11. **t_issue_process** - 问题处理记录表：存储问题处理记录
12. **t_notification** - 消息通知表：存储系统消息

系统遵循最小修改原则，尽可能复用现有数据表结构。对于新增功能，建议通过扩展现有表或增加关联表实现，避免大范围修改数据结构。

## 11. MySQL乱码问题解决方案

您的SQL文件中出现乱码是因为字符集编码问题。虽然您在导出时指定了`--default-character-set=utf8mb4`，但可能仍存在编码不一致的情况。

### 问题原因

1. MySQL服务器的字符集设置
2. 数据库创建时的字符集设置
3. 导出和导入过程中的字符集不匹配
4. 查看工具的字符集不匹配

### 解决方案

#### 方案一：修正导出命令

```bash
mysqldump -u root -p --default-character-set=utf8mb4 --set-charset --skip-set-charset check_app > check_app_20250610.sql
```

#### 方案二：确保MySQL服务器配置

在MySQL配置文件(my.cnf或my.ini)中设置：

```ini
[client]
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4

[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
```

#### 方案三：重新导出时指定更完整的参数

```bash
mysqldump -u root -p --default-character-set=utf8mb4 --set-charset --skip-set-charset --hex-blob check_app > check_app_20250610_fixed.sql
```

#### 方案四：数据库恢复时指定字符集

```bash
mysql -u root -p --default-character-set=utf8mb4 check_app < check_app_20250610.sql
```

#### 方案五：检查和修复数据库表的字符集

```sql
ALTER DATABASE check_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 对每个表执行以下操作
ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

通过以上方案，您可以解决MySQL导出SQL文件时出现乱码的问题，确保中文字符正确显示。