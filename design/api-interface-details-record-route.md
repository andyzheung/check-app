# 巡检系统API接口详细文档（巡检记录路径部分）

## 概述

本文档详细说明巡检系统巡检记录路径相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 巡检记录路径相关接口

### 1.1 获取巡检记录详情（含路径数据） [COMMON]

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

### 1.2 提交巡检路径 [APP]

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

### 1.3 获取巡检路径 [COMMON]

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

## 2. 巡检路径数据说明

巡检路径功能通过`t_inspection_record`表中的`route_path`字段存储路径数据，采用JSON数组格式。每个路径点包含以下信息：

| 字段 | 类型 | 说明 |
|------|------|------|
| timestamp | String | 时间戳，格式：yyyy-MM-dd HH:mm:ss |
| latitude | Double | 纬度 |
| longitude | Double | 经度 |
| location | String | 位置描述（可选） |

巡检路径数据主要用于记录巡检人员在巡检过程中的移动轨迹，可用于管理后台查看巡检情况，确认巡检是否按规定路线进行。移动应用通过定时记录位置信息，在巡检结束后提交完整的路径数据。 