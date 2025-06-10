# 巡检系统API接口详细文档（问题处理图片部分）

## 概述

本文档详细说明巡检系统问题处理图片相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 问题处理图片相关接口

### 1.1 获取问题处理记录（含图片URL数组） [COMMON]

**接口路径**: `GET /api/v1/issues/{id}/processes`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 问题ID |

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
      },
      {
        "id": 2,
        "issueId": 1,
        "action": "process",
        "processorId": 3,
        "processorName": "王五",
        "processTime": "2025-06-01 10:30:00",
        "content": "检查空调系统，发现滤网堵塞",
        "images": [
          "/data/uploads/2025/06/issue1_3.jpg"
        ],
        "createTime": "2025-06-01 10:30:00"
      }
    ]
  }
}
```

### 1.2 处理问题（可提交图片URL数组） [COMMON]

**接口路径**: `POST /api/v1/issues/{id}/process`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 问题ID |

**请求参数**:

```json
{
  "action": "process",
  "content": "检查空调系统，发现滤网堵塞",
  "images": [
    "/data/uploads/2025/06/issue1_3.jpg"
  ]
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "处理成功",
  "data": {
    "id": 2,
    "issueId": 1,
    "action": "process",
    "processorId": 3,
    "processorName": "王五",
    "processTime": "2025-06-01 10:30:00",
    "content": "检查空调系统，发现滤网堵塞",
    "images": [
      "/data/uploads/2025/06/issue1_3.jpg"
    ],
    "createTime": "2025-06-01 10:30:00"
  }
}
```

### 1.3 上传问题处理图片 [COMMON]

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

### 1.4 获取问题处理图片 [COMMON]

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

### 1.5 删除问题处理图片 [COMMON]

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

## 2. 问题处理图片实现说明

问题处理图片功能通过`t_issue_process`表中的`images`字段存储图片URL数组（JSON格式），同时使用`t_file_upload`表存储图片文件信息。处理问题时可以同时上传多张图片，也可以在创建处理记录后通过专门的接口上传图片。

图片URL存储格式为JSON数组：
```json
[
  "/data/uploads/2025/06/issue1_1.jpg",
  "/data/uploads/2025/06/issue1_2.jpg"
]
```

上传的图片文件存储在文件系统中，文件路径由系统配置决定，默认为`/data/uploads/yyyy/MM/`目录。 