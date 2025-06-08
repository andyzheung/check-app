# 个人中心与首页设计文档

## 1. 功能概述

### 1.1 个人中心
提供用户个人信息查看和管理功能，包括：
- 基本信息展示（工号、姓名、部门等）
- 消息通知管理
- 关于页面（版本信息、帮助等）

### 1.2 首页
展示用户的巡检任务和可巡检区域：
- 今日巡检任务统计
- 待完成任务列表
- 已完成任务列表
- 可巡检区域列表

## 2. 数据模型

### 2.1 复用现有表结构
1. `t_user` 表：用户信息
   ```sql
   CREATE TABLE t_user (
     id BIGINT PRIMARY KEY,
     username VARCHAR(50) NOT NULL,
     real_name VARCHAR(50),
     password VARCHAR(100),
     role VARCHAR(20),
     department VARCHAR(50),
     status VARCHAR(20),
     create_time DATETIME,
     update_time DATETIME,
     deleted TINYINT DEFAULT 0
   );
   ```

2. `t_area` 表：区域信息
   ```sql
   CREATE TABLE t_area (
     id BIGINT PRIMARY KEY,
     area_code VARCHAR(50) NOT NULL,
     area_name VARCHAR(100) NOT NULL,
     area_type VARCHAR(50),
     status VARCHAR(20),
     description TEXT,
     qr_code_url VARCHAR(255),
     create_time DATETIME,
     update_time DATETIME,
     deleted TINYINT DEFAULT 0
   );
   ```

3. `t_inspection_record` 表：巡检记录
   ```sql
   CREATE TABLE t_inspection_record (
     id BIGINT PRIMARY KEY,
     record_no VARCHAR(50) NOT NULL,
     area_id BIGINT NOT NULL,
     inspector_id BIGINT NOT NULL,
     start_time DATETIME,
     end_time DATETIME,
     status VARCHAR(20),
     remark TEXT,
     create_time DATETIME,
     update_time DATETIME,
     deleted TINYINT DEFAULT 0
   );
   ```

### 2.2 新增消息通知表
```sql
CREATE TABLE t_notification (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content TEXT,
  type VARCHAR(20),
  status VARCHAR(20),
  create_time DATETIME,
  update_time DATETIME,
  deleted TINYINT DEFAULT 0
);
```

## 3. API 设计

### 3.1 个人中心 API

#### 3.1.1 获取当前用户信息
```
GET /api/v1/users/current

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "用户ID",
    "username": "用户名",
    "realName": "真实姓名",
    "department": "所属部门",
    "role": "角色",
    "status": "状态"
  }
}
```

#### 3.1.2 获取消息通知列表
```
GET /api/v1/notifications?page=1&size=10

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": "通知ID",
        "title": "通知标题",
        "content": "通知内容",
        "type": "通知类型",
        "status": "通知状态",
        "createTime": "创建时间"
      }
    ],
    "total": 100
  }
}
```

### 3.2 首页 API

#### 3.2.1 获取今日巡检任务统计
```
GET /api/v1/tasks/today/stats

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "totalTasks": 5,
    "completedTasks": 2,
    "pendingTasks": 3
  }
}
```

#### 3.2.2 获取巡检任务列表
```
GET /api/v1/tasks?status=pending&page=1&size=10

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": "任务ID",
        "areaId": "区域ID",
        "areaName": "区域名称",
        "status": "任务状态",
        "startTime": "开始时间",
        "endTime": "结束时间"
      }
    ],
    "total": 100
  }
}
```

#### 3.2.3 获取可巡检区域列表
```
GET /api/v1/areas?status=active&page=1&size=10

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": "区域ID",
        "areaCode": "区域编码",
        "areaName": "区域名称",
        "areaType": "区域类型",
        "status": "区域状态"
      }
    ],
    "total": 100
  }
}
```

## 4. 前端设计

### 4.1 个人中心页面（Profile.vue）
```vue
<template>
  <div class="profile-container">
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="avatar">{{ userInfo.realName[0] }}</div>
      <div class="user-info">
        <div class="name">{{ userInfo.realName }}</div>
        <div class="department">{{ userInfo.department }}</div>
      </div>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-list">
      <div class="menu-item" @click="goToPersonalInfo">
        <span>个人信息</span>
        <span class="material-icons">chevron_right</span>
      </div>
      <div class="menu-item" @click="goToNotifications">
        <span>消息通知</span>
        <span class="material-icons">chevron_right</span>
      </div>
      <div class="menu-item" @click="goToAbout">
        <span>关于</span>
        <span class="material-icons">chevron_right</span>
      </div>
    </div>

    <!-- 退出登录按钮 -->
    <div class="logout-btn" @click="handleLogout">
      退出登录
    </div>
  </div>
</template>
```

### 4.2 首页（Home.vue）
```vue
<template>
  <div class="home-container">
    <!-- 任务统计 -->
    <div class="stats-card">
      <div class="stats-item">
        <div class="stats-value">{{ stats.totalTasks }}</div>
        <div class="stats-label">今日任务</div>
      </div>
      <div class="stats-item">
        <div class="stats-value">{{ stats.completedTasks }}</div>
        <div class="stats-label">已完成</div>
      </div>
      <div class="stats-item">
        <div class="stats-value">{{ stats.pendingTasks }}</div>
        <div class="stats-label">待完成</div>
      </div>
    </div>

    <!-- 任务列表 -->
    <div class="task-list">
      <div class="section-title">待完成任务</div>
      <div class="task-card" v-for="task in pendingTasks" :key="task.id">
        <div class="task-info">
          <div class="area-name">{{ task.areaName }}</div>
          <div class="task-time">{{ formatTime(task.startTime) }}</div>
        </div>
        <button class="start-btn" @click="startInspection(task)">开始巡检</button>
      </div>
    </div>

    <!-- 区域列表 -->
    <div class="area-list">
      <div class="section-title">巡检区域</div>
      <div class="area-card" v-for="area in areas" :key="area.id">
        <div class="area-info">
          <div class="area-name">{{ area.areaName }}</div>
          <div class="area-code">{{ area.areaCode }}</div>
        </div>
        <div class="area-type">{{ area.areaType }}</div>
      </div>
    </div>
  </div>
</template>
```

## 5. 技术实现要点

### 5.1 后端实现
1. 使用 Spring Cache 缓存用户信息
2. 使用 Spring Security 处理权限验证
3. 使用 MyBatis-Plus 实现数据访问
4. 实现统一的异常处理和响应格式

### 5.2 前端实现
1. 使用 Vue Router 管理路由
2. 使用 Vuex/Pinia 管理状态
3. 使用 Axios 处理 HTTP 请求
4. 实现响应式布局，适配不同屏幕尺寸

## 6. 安全性考虑
1. 接口访问需要 Token 验证
2. 敏感信息（如密码）需要加密存储
3. 防止 XSS 和 CSRF 攻击
4. 实现请求频率限制

## 7. 性能优化
1. 合理使用缓存
2. 分页加载数据
3. 图片懒加载
4. 减少不必要的 API 请求

## 8. 后续优化建议
1. 添加消息推送功能
2. 支持头像上传
3. 添加任务提醒功能
4. 优化数据加载性能
5. 添加数据统计和分析功能 