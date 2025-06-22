# 巡检系统API接口详细文档（系统参数部分）

## 概述

本文档详细说明巡检系统系统参数相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 系统参数相关接口

### 1.1 获取所有系统参数 [ADMIN]

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

### 1.2 获取指定系统参数 [COMMON]

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

### 1.3 设置系统参数 [ADMIN]

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

### 1.4 删除系统参数 [ADMIN]

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

## 2. 系统参数使用说明

系统参数主要用于存储全局配置信息，包括但不限于以下参数：

### 2.1 系统基本参数

| 参数键 | 说明 | 默认值 |
|--------|------|--------|
| system.name | 系统名称 | 巡检管理系统 |
| system.version | 系统版本 | v1.0.7 |

### 2.2 任务相关参数

| 参数键 | 说明 | 默认值 |
|--------|------|--------|
| task.auto_assign | 是否自动分配任务 | true |
| task.reminder_minutes | 任务提醒提前分钟数 | 30 |

### 2.3 通知相关参数

| 参数键 | 说明 | 默认值 |
|--------|------|--------|
| notification.enable | 是否开启消息通知 | true |

### 2.4 文件上传相关参数

| 参数键 | 说明 | 默认值 |
|--------|------|--------|
| file.upload.path | 文件上传路径 | /data/uploads |
| file.allowed_types | 允许上传的文件类型 | jpg,jpeg,png,pdf,doc,docx |
| file.max_size | 最大文件大小(字节) | 10485760 (10MB) | 