# 巡检排班功能设计文档

## 1. 功能概述
巡检排班功能用于管理巡检人员的排班计划，支持查看今日任务、巡检区域等信息。主要包含以下功能：
- 排班管理（新增、编辑、删除排班）
- 今日任务统计（待完成/已完成）
- 巡检区域展示
- 个人中心信息展示

## 2. 数据库设计

### 2.1 排班表(t_inspection_schedule)
| 字段名 | 类型 | 说明 | 
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| area_id | BIGINT | 区域ID |
| schedule_date | DATE | 排班日期 |
| shift | VARCHAR(20) | 班次 |
| status | VARCHAR(20) | 状态(pending/completed) |
| remark | VARCHAR(500) | 备注 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| deleted | TINYINT | 逻辑删除标志 |

## 3. 接口设计

### 3.1 获取今日排班
```
GET /api/v1/schedules/today
```
响应数据:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "schedules": [{
      "id": 1,
      "userId": 1,
      "userName": "张三",
      "areaId": 1,
      "areaName": "A区机房",
      "scheduleDate": "2024-03-21",
      "shift": "morning",
      "status": "pending"
    }],
    "pendingTasks": 1,
    "completedTasks": 0
  }
}
```

### 3.2 获取用户信息
```
GET /api/v1/users/current
```
响应数据:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "realName": "张三",
    "phone": "13800138000",
    "email": "zhangsan@example.com",
    "department": "运维部",
    "role": "inspector"
  }
}
```

## 4. 前端设计

### 4.1 页面结构
- 主页(Home.vue)
  - 今日巡检任务卡片
  - 巡检区域卡片
  - 扫码按钮
  - 底部导航栏
- 个人中心(Profile.vue)
  - 用户基本信息
  - 功能菜单
  - 退出登录按钮
  - 底部导航栏

### 4.2 数据流
1. 主页加载时:
   - 调用/api/v1/schedules/today获取今日任务
   - 调用/api/v1/areas获取巡检区域
2. 个人中心加载时:
   - 调用/api/v1/users/current获取用户信息

## 5. 代码结构

### 5.1 后端代码
```
com.pensun.checkapp
├── controller
│   └── ScheduleController.java
├── service
│   ├── ScheduleService.java
│   └── impl/ScheduleServiceImpl.java
├── mapper
│   └── ScheduleMapper.java
├── entity
│   └── InspectionSchedule.java
└── dto
    └── ScheduleDTO.java
```

### 5.2 前端代码
```
src
├── views
│   ├── Home.vue
│   └── Profile.vue
├── utils
│   └── request.js
└── stores
    └── user.js
``` 