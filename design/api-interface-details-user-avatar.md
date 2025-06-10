# 巡检系统API接口详细文档（用户头像部分）

## 概述

本文档详细说明巡检系统用户头像相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 用户头像相关接口

### 1.1 获取用户详情（含头像URL） [COMMON]

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

### 1.2 获取当前用户信息（含头像URL） [COMMON]

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

### 1.3 上传用户头像 [COMMON]

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

### 1.4 获取用户头像 [COMMON]

**接口路径**: `GET /api/v1/users/{id}/avatar`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 用户ID |

**响应**: 直接返回图片文件

## 2. 实现说明

用户头像功能利用`t_user`表中的`avatar`字段存储头像URL，同时使用`t_file_upload`表存储头像文件信息。上传头像时会将文件存储到指定目录，并将URL更新到用户表中。

头像URL格式可以是以下形式之一：
- 相对路径：`/data/uploads/2025/06/user_1.jpg`
- 绝对URL：`https://api.example.com/avatars/default-1.png`

通过`/api/v1/users/{id}/avatar`接口获取头像时，系统会根据存储的URL直接返回图片文件。 