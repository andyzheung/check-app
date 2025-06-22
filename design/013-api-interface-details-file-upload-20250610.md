# 巡检系统API接口详细文档（文件上传部分）

## 概述

本文档详细说明巡检系统文件上传相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

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