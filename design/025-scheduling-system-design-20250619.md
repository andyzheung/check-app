# 排班系统设计文档

## 1. 概述

排班系统是巡检App管理后台的核心功能模块，用于管理员对巡检人员进行排班管理，确保各区域的巡检工作有序进行。通过该系统，管理员可以安排不同角色的人员在特定时间对特定区域进行巡检，并跟踪任务的完成情况。

## 2. 用户角色与权限

### 2.1 管理员
- 可以创建、编辑、删除排班计划
- 可以查看所有排班记录和统计信息
- 可以为任何区域安排巡检人员

### 2.2 运维人员
- 只能查看自己的排班计划
- 可以查看自己负责区域的排班情况
- 不能修改排班计划

### 2.3 普通用户
- 不能访问排班系统

## 3. 功能模块

### 3.1 排班管理
- 日历视图：以日历形式展示排班计划
- 列表视图：以列表形式展示排班计划
- 新增排班：选择区域、选择人员、选择日期、选择班次
- 编辑排班：修改已有排班计划
- 删除排班：删除单个或批量排班计划
- 复制排班：将某天或某周的排班复制到其他时间段

### 3.2 班次设置
- 早班（Morning Shift）：08:00-16:00
- 午班（Afternoon Shift）：16:00-24:00
- 夜班（Night Shift）：00:00-08:00

### 3.3 人员选择
- 按部门筛选
- 按角色筛选
- 按技能筛选
- 批量选择

### 3.4 排班冲突检测
- 同一时间段人员重复排班检测
- 连续排班疲劳预警
- 休息时间不足提醒

### 3.5 排班统计
- 人员工作量统计
- 区域覆盖率统计
- 班次分布统计
- 导出统计报表

## 4. 页面设计

### 4.1 排班计划页面
- 顶部：筛选条件（日期范围、区域、人员、班次）
- 中间：日历视图/列表视图切换
- 日历视图：按月、按周、按日展示，可拖拽排班
- 列表视图：分页展示排班记录，支持排序和筛选
- 底部：统计信息展示
- 右上角：新增、批量操作等功能按钮

### 4.2 新增/编辑排班弹窗
- 基本信息：日期、班次、状态
- 区域选择：下拉选择区域
- 人员选择：支持搜索、多选
- 重复设置：单次、每天、每周、每月、自定义
- 备注信息：文本输入框

### 4.3 排班统计页面
- 人员工作量图表：柱状图/饼图
- 区域覆盖率图表：热力图
- 班次分布图表：柱状图
- 筛选条件：时间范围、部门、区域
- 导出功能：导出Excel/PDF报表

## 5. 数据库设计

### 5.1 现有表结构
```sql
CREATE TABLE IF NOT EXISTS t_inspection_schedule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    area_id BIGINT NOT NULL COMMENT '区域ID',
    schedule_date DATE NOT NULL COMMENT '排班日期',
    shift VARCHAR(20) NOT NULL COMMENT '班次：morning-早班，afternoon-午班，night-夜班',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待完成，completed-已完成',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_user_date (user_id, schedule_date),
    KEY idx_area_date (area_id, schedule_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检排班表';
```

### 5.2 新增表结构
无需新增表结构，现有表结构已满足基本需求。

## 6. API设计

### 6.1 排班管理接口

#### 6.1.1 获取排班列表
- 请求方式：GET
- 接口路径：/api/v1/schedules
- 请求参数：
  ```json
  {
    "startDate": "2025-06-01",
    "endDate": "2025-06-30",
    "areaId": 1,
    "userId": 2,
    "shift": "morning",
    "status": "pending",
    "pageNum": 1,
    "pageSize": 10
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 100,
      "list": [
        {
          "id": 1,
          "userId": 2,
          "userName": "张三",
          "areaId": 1,
          "areaName": "A区机房",
          "scheduleDate": "2025-06-01",
          "shift": "morning",
          "status": "pending",
          "remark": "例行巡检",
          "createTime": "2025-06-01 10:00:00"
        }
      ]
    }
  }
  ```

#### 6.1.2 获取日历视图排班数据
- 请求方式：GET
- 接口路径：/api/v1/schedules/calendar
- 请求参数：
  ```json
  {
    "year": 2025,
    "month": 6,
    "areaId": 1
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "date": "2025-06-01",
        "schedules": [
          {
            "id": 1,
            "userId": 2,
            "userName": "张三",
            "shift": "morning",
            "status": "pending"
          },
          {
            "id": 2,
            "userId": 3,
            "userName": "李四",
            "shift": "afternoon",
            "status": "pending"
          }
        ]
      }
    ]
  }
  ```

#### 6.1.3 创建排班
- 请求方式：POST
- 接口路径：/api/v1/schedules
- 请求参数：
  ```json
  {
    "userId": 2,
    "areaId": 1,
    "scheduleDate": "2025-06-01",
    "shift": "morning",
    "remark": "例行巡检"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1
    }
  }
  ```

#### 6.1.4 批量创建排班
- 请求方式：POST
- 接口路径：/api/v1/schedules/batch
- 请求参数：
  ```json
  {
    "userIds": [2, 3],
    "areaId": 1,
    "startDate": "2025-06-01",
    "endDate": "2025-06-07",
    "shift": "morning",
    "remark": "例行巡检"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "count": 14
    }
  }
  ```

#### 6.1.5 更新排班
- 请求方式：PUT
- 接口路径：/api/v1/schedules/{id}
- 请求参数：
  ```json
  {
    "userId": 2,
    "areaId": 1,
    "scheduleDate": "2025-06-01",
    "shift": "afternoon",
    "status": "pending",
    "remark": "调整班次"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.1.6 删除排班
- 请求方式：DELETE
- 接口路径：/api/v1/schedules/{id}
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.1.7 获取排班统计
- 请求方式：GET
- 接口路径：/api/v1/schedules/statistics
- 请求参数：
  ```json
  {
    "startDate": "2025-06-01",
    "endDate": "2025-06-30",
    "areaId": 1
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "userWorkload": [
        {
          "userId": 2,
          "userName": "张三",
          "count": 10
        }
      ],
      "areaDistribution": [
        {
          "areaId": 1,
          "areaName": "A区机房",
          "count": 20
        }
      ],
      "shiftDistribution": {
        "morning": 15,
        "afternoon": 15,
        "night": 10
      }
    }
  }
  ```

## 7. 前端组件设计

### 7.1 主要组件
- ScheduleCalendar：日历视图组件
- ScheduleList：列表视图组件
- ScheduleForm：排班表单组件
- UserSelector：用户选择组件
- AreaSelector：区域选择组件
- ShiftSelector：班次选择组件
- StatisticsChart：统计图表组件

### 7.2 状态管理
- scheduleState：存储排班数据
- filterState：存储筛选条件
- userState：存储用户数据
- areaState：存储区域数据

## 8. 实现计划

### 8.1 阶段一：后端开发
- 完善ScheduleController，实现所有API接口
- 增强ScheduleService，支持批量操作和统计功能
- 增加单元测试和集成测试

### 8.2 阶段二：前端开发
- 实现排班日历视图组件
- 实现排班列表视图组件
- 实现排班表单和操作功能
- 实现统计图表功能

### 8.3 阶段三：测试和优化
- 功能测试
- 性能优化
- 用户体验优化

## 9. 其他考虑

### 9.1 性能考虑
- 日历视图可能包含大量数据，需要分页或虚拟滚动
- 批量操作可能涉及大量数据处理，需要异步处理
- 统计计算可能耗时，考虑缓存机制

### 9.2 扩展性考虑
- 考虑未来可能增加的高级排班功能，如自动排班、轮班规则等
- 预留接口和数据结构，方便未来功能扩展

### 9.3 用户体验考虑
- 操作需要简单直观
- 提供丰富的快捷操作
- 排班冲突提示和解决建议
- 响应式设计，适应不同设备

## 10. AD域账号与排班系统集成补充

### 10.1 AD用户排班特殊处理
- 仅同步为"运维人员"角色的AD用户可参与排班，角色变更后自动刷新排班权限。
- AD用户被禁用/离职时，系统自动移除其未完成排班任务，管理员收到通知。

### 10.2 权限同步与异常场景
- 排班相关操作权限（如schedule_view、schedule_edit）可通过AD组动态赋予。
- 若AD同步异常导致用户权限丢失，系统自动锁定相关排班操作并提示管理员。
- 支持批量刷新排班权限，确保AD变更后排班系统权限一致。

### 10.3 审计与安全
- 所有因AD同步导致的排班变更、权限调整均需记录审计日志。
- 支持排班权限变更通知，便于运维和管理员及时响应。 