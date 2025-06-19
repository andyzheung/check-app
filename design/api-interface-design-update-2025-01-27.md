# 巡检系统接口设计文档 - 更新版

**更新日期**: 2025-01-27  
**版本**: v2.0  
**更新内容**: 支持数据中心和弱电间两种巡检类型

## 1. 概述

本文档定义了巡检系统前后端接口设计，基于现有架构进行扩展设计，支持数据中心和弱电间两种不同类型的巡检需求。

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

## 2. 区域管理接口

### 2.1 获取区域列表（支持按类型筛选）

**接口**: `GET /areas`

**请求参数**:
- areaType: 区域类型（可选）：datacenter-数据中心，weakroom-弱电间
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- keyword: 搜索关键词（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 20,
    "list": [
      {
        "id": 1,
        "code": "DC001",
        "name": "A区数据中心",
        "description": "主数据中心机房",
        "address": "1楼A区",
        "areaType": "datacenter",
        "moduleCount": 4,
        "status": "active",
        "qrCodeUrl": "https://example.com/qr/DC001.png",
        "createTime": "2025-01-01 00:00:00"
      },
      {
        "id": 2,
        "code": "WR001", 
        "name": "B区弱电间",
        "description": "网络设备机房",
        "address": "2楼B区",
        "areaType": "weakroom",
        "moduleCount": 0,
        "status": "active",
        "qrCodeUrl": "https://example.com/qr/WR001.png",
        "createTime": "2025-01-01 00:00:00"
      }
    ]
  }
}
```

### 2.2 获取区域详情

**接口**: `GET /areas/{id}`

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "code": "DC001",
    "name": "A区数据中心",
    "description": "主数据中心机房",
    "address": "1楼A区",
    "areaType": "datacenter",
    "moduleCount": 4,
    "configJson": {
      "modules": [
        {"id": 1, "name": "模块1", "items": ["return_air_temp", "power_status"]},
        {"id": 2, "name": "模块2", "items": ["return_air_temp", "power_status"]}
      ]
    },
    "status": "active",
    "qrCodeUrl": "https://example.com/qr/DC001.png",
    "createTime": "2025-01-01 00:00:00"
  }
}
```

### 2.3 通过二维码获取区域信息

**接口**: `GET /areas/qrcode/{code}`

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "code": "DC001",
    "name": "A区数据中心",
    "areaType": "datacenter",
    "moduleCount": 4,
    "inspectionItems": [
      {
        "id": 1,
        "itemCode": "sound_light_alarm",
        "itemName": "机房范围内是否有声光报警",
        "itemType": "boolean",
        "groupName": "安全检查",
        "isRequired": true,
        "sortOrder": 1
      }
    ]
  }
}
```

## 3. 巡检项目配置接口

### 3.1 获取巡检项目配置

**接口**: `GET /inspection-items`

**请求参数**:
- areaType: 区域类型（必填）：datacenter 或 weakroom
- groupName: 分组名称（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "areaType": "datacenter",
      "itemCode": "sound_light_alarm",
      "itemName": "机房范围内是否有声光报警",
      "itemType": "boolean",
      "groupName": "安全检查",
      "isRequired": true,
      "sortOrder": 1,
      "status": "active"
    },
    {
      "id": 2,
      "areaType": "datacenter",
      "itemCode": "room_temperature",
      "itemName": "机房环境温度",
      "itemType": "number",
      "groupName": "环境检查",
      "isRequired": true,
      "sortOrder": 5,
      "status": "active"
    }
  ]
}
```

### 3.2 获取分组巡检项目

**接口**: `GET /inspection-items/grouped`

**请求参数**:
- areaType: 区域类型（必填）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "安全检查": [
      {
        "id": 1,
        "itemCode": "sound_light_alarm",
        "itemName": "机房范围内是否有声光报警",
        "itemType": "boolean",
        "isRequired": true,
        "sortOrder": 1
      }
    ],
    "环境检查": [
      {
        "id": 2,
        "itemCode": "room_temperature",
        "itemName": "机房环境温度",
        "itemType": "number",
        "isRequired": true,
        "sortOrder": 5
      }
    ]
  }
}
```

## 4. 巡检记录接口

### 4.1 创建巡检记录

**接口**: `POST /inspection-records`

**请求参数**:

```json
{
  "areaId": 1,
  "areaType": "datacenter",
  "inspectionItems": [
    {
      "itemId": 1,
      "itemCode": "sound_light_alarm",
      "itemName": "机房范围内是否有声光报警",
      "itemValue": "false",
      "isNormal": 0
    },
    {
      "itemId": 2,
      "itemCode": "room_temperature",
      "itemName": "机房环境温度",
      "itemValue": "25.5",
      "isNormal": 1
    }
  ],
  "remark": "发现声光报警异常，已记录"
}
```

**响应**:

```json
{
  "code": 200,
  "message": "巡检记录创建成功",
  "data": {
    "id": 1001,
    "recordNo": "IR20250127001",
    "areaId": 1,
    "areaType": "datacenter",
    "totalItems": 5,
    "normalItems": 4,
    "abnormalItems": 1,
    "status": "completed",
    "createTime": "2025-01-27 10:30:00"
  }
}
```

### 4.2 获取巡检记录列表

**接口**: `GET /inspection-records`

**请求参数**:
- pageNum: 页码，默认1
- pageSize: 每页记录数，默认10
- areaType: 区域类型（可选）
- areaId: 区域ID（可选）
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）
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
        "id": 1001,
        "recordNo": "IR20250127001",
        "areaId": 1,
        "areaName": "A区数据中心",
        "areaType": "datacenter",
        "inspectorId": 1,
        "inspectorName": "张三",
        "totalItems": 5,
        "normalItems": 4,
        "abnormalItems": 1,
        "status": "completed",
        "remark": "发现声光报警异常，已记录",
        "startTime": "2025-01-27 10:30:00",
        "endTime": "2025-01-27 10:45:00",
        "createTime": "2025-01-27 10:30:00"
      }
    ]
  }
}
```

### 4.3 获取巡检记录详情

**接口**: `GET /inspection-records/{id}`

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1001,
    "recordNo": "IR20250127001",
    "areaId": 1,
    "areaName": "A区数据中心",
    "areaType": "datacenter",
    "inspectorId": 1,
    "inspectorName": "张三",
    "totalItems": 5,
    "normalItems": 4,
    "abnormalItems": 1,
    "status": "completed",
    "remark": "发现声光报警异常，已记录",
    "startTime": "2025-01-27 10:30:00",
    "endTime": "2025-01-27 10:45:00",
    "createTime": "2025-01-27 10:30:00",
    "inspectionDetails": [
      {
        "id": 1,
        "itemId": 1,
        "itemCode": "sound_light_alarm",
        "itemName": "机房范围内是否有声光报警",
        "itemValue": "false",
        "isNormal": 0,
        "groupName": "安全检查"
      },
      {
        "id": 2,
        "itemId": 2,
        "itemCode": "room_temperature",
        "itemName": "机房环境温度",
        "itemValue": "25.5",
        "isNormal": 1,
        "groupName": "环境检查"
      }
    ]
  }
}
```

## 5. 统计分析接口

### 5.1 获取巡检统计数据

**接口**: `GET /statistics/inspection`

**请求参数**:
- startTime: 开始时间（可选）
- endTime: 结束时间（可选）
- areaType: 区域类型（可选）
- areaId: 区域ID（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "overview": {
      "totalRecords": 100,
      "normalRecords": 85,
      "abnormalRecords": 15,
      "completionRate": 95.5
    },
    "byAreaType": {
      "datacenter": {
        "totalRecords": 60,
        "normalRecords": 50,
        "abnormalRecords": 10,
        "completionRate": 96.0
      },
      "weakroom": {
        "totalRecords": 40,
        "normalRecords": 35,
        "abnormalRecords": 5,
        "completionRate": 94.5
      }
    },
    "abnormalItems": [
      {
        "itemCode": "sound_light_alarm",
        "itemName": "机房范围内是否有声光报警",
        "abnormalCount": 5,
        "totalCount": 60,
        "abnormalRate": 8.3
      }
    ]
  }
}
```

### 5.2 获取趋势统计

**接口**: `GET /statistics/trend`

**请求参数**:
- period: 统计周期（day/week/month）
- startTime: 开始时间
- endTime: 结束时间
- areaType: 区域类型（可选）

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "timeline": ["2025-01-20", "2025-01-21", "2025-01-22", "2025-01-23"],
    "datacenter": {
      "totalRecords": [10, 12, 8, 15],
      "abnormalRecords": [1, 2, 0, 3]
    },
    "weakroom": {
      "totalRecords": [8, 9, 6, 11],
      "abnormalRecords": [0, 1, 1, 2]
    }
  }
}
```

## 6. 数据中心模块管理接口

### 6.1 获取区域模块配置

**接口**: `GET /areas/{areaId}/modules`

**响应**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "模块1",
      "items": [
        {
          "itemCode": "module1_return_air_temp",
          "itemName": "模块1回风温度",
          "itemType": "number"
        },
        {
          "itemCode": "module1_power_status",
          "itemName": "模块1供电状态",
          "itemType": "boolean"
        }
      ]
    }
  ]
}
```

### 6.2 更新区域模块配置

**接口**: `PUT /areas/{areaId}/modules`

**请求参数**:

```json
{
  "moduleCount": 4,
  "modules": [
    {
      "id": 1,
      "name": "模块1",
      "items": ["return_air_temp", "power_status"]
    },
    {
      "id": 2,
      "name": "模块2",
      "items": ["return_air_temp", "power_status"]
    }
  ]
}
```

**响应**:

```json
{
  "code": 200,
  "message": "模块配置更新成功",
  "data": null
}
```

## 7. 错误码定义

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 4001 | 区域类型不支持 |
| 4002 | 巡检项目配置不存在 |
| 4003 | 巡检记录不存在 |
| 4004 | 模块配置错误 |
| 500 | 服务器内部错误 |

## 8. 兼容性说明

### 8.1 现有接口兼容

现有的区域和巡检记录接口保持兼容，新增的字段使用默认值：
- 区域类型默认为 `datacenter`
- 模块数量默认为 0
- 配置信息默认为空

### 8.2 渐进式迁移

支持渐进式迁移，可以逐步将现有区域配置为对应的类型，不影响现有功能。

## 9. 性能优化建议

1. **缓存策略**: 巡检项目配置可以缓存30分钟
2. **批量查询**: 支持批量获取多个区域的巡检项目
3. **分页查询**: 所有列表接口都支持分页
4. **索引优化**: 基于区域类型和时间的复合索引 