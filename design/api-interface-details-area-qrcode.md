# 巡检系统API接口详细文档（区域二维码部分）

## 概述

本文档详细说明巡检系统区域二维码相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 区域二维码相关接口

### 1.1 获取区域详情（含二维码URL） [COMMON]

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

### 1.2 获取区域列表（含二维码URL） [COMMON]

**接口路径**: `GET /api/v1/areas`

**请求参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| type | String | 否 | 区域类型 |
| status | String | 否 | 区域状态 |
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
      }
    ],
    "pageNum": 1,
    "pageSize": 10,
    "pages": 1
  }
}
```

### 1.3 获取区域二维码 [COMMON]

**接口路径**: `GET /api/v1/areas/{id}/qrcode`

**路径参数**:

| 参数名 | 类型 | 必选 | 说明 |
|-------|------|------|------|
| id | Long | 是 | 区域ID |

**响应**: 直接返回二维码图片

### 1.4 生成区域二维码 [ADMIN]

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

## 2. 区域二维码使用说明

区域二维码功能通过`t_area`表中的`qr_code_url`字段存储二维码URL。二维码内容通常包含区域ID或区域编码，用于APP扫码快速识别区域，进行巡检操作。

二维码URL格式可以是以下形式之一：
- 相对路径：`/data/uploads/qrcodes/area_1.png`
- 绝对URL：`https://api.example.com/qrcode/area/1`

通过`/api/v1/areas/{id}/qrcode`接口获取二维码时，系统会根据存储的URL直接返回图片文件，或者根据区域ID动态生成二维码图片。 