# 巡检系统API接口详细文档（统计数据部分）

## 概述

本文档详细说明巡检系统统计数据相关API接口的请求和响应格式，所有接口均遵循统一的响应格式。

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 1. 统计数据相关接口

### 1.1 获取每日巡检统计 [COMMON]

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

### 1.2 获取每周巡检统计 [COMMON]

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

### 1.3 获取问题统计 [COMMON]

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

### 1.4 获取仪表盘数据 [ADMIN]

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

### 1.5 刷新统计缓存 [ADMIN]

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

## 2. 统计数据缓存说明

系统使用`t_statistics_cache`表来缓存统计数据，以提高查询性能。缓存项包括：

| 缓存键 | 说明 | 缓存时间 |
|-------|------|----------|
| inspection_count_daily | 每日巡检统计 | 1天 |
| inspection_count_weekly | 每周巡检统计 | 7天 |
| issue_statistics | 问题统计 | 1天 |
| dashboard_data | 仪表盘数据 | 1小时 |

当缓存过期或手动刷新时，系统会重新计算并缓存数据。 