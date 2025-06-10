# 巡检系统接口分类文档

## 概述

本文档对巡检系统的API接口进行分类，明确区分哪些接口用于移动应用(App)与服务器(Check-app-server)交互，哪些接口用于管理后台(Admin-UI)与服务器交互，以及两者共用的接口。

## 接口分类标识说明

在API接口设计文档中，我们使用以下标识来区分接口用途：

- **[APP]** - 表示该接口主要供移动应用使用
- **[ADMIN]** - 表示该接口主要供管理后台使用
- **[COMMON]** - 表示该接口为移动应用和管理后台共用

## 接口分类汇总表

以下是按功能模块划分的接口分类汇总：

### 1. 认证相关接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/auth/login` | POST | 用户登录 | [COMMON] |
| `/api/v1/auth/info` | GET | 获取当前用户信息 | [COMMON] |
| `/api/v1/auth/logout` | POST | 退出登录 | [COMMON] |

### 2. 用户管理接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/users` | GET | 获取用户列表 | [ADMIN] |
| `/api/v1/users/{id}` | GET | 获取用户详情 | [COMMON] |
| `/api/v1/users` | POST | 创建用户 | [ADMIN] |
| `/api/v1/users/{id}` | PUT | 更新用户 | [ADMIN] |
| `/api/v1/users/{id}` | DELETE | 删除用户 | [ADMIN] |
| `/api/v1/users/{id}/avatar` | POST | 上传用户头像 | [COMMON] |
| `/api/v1/users/{id}/avatar` | GET | 获取用户头像 | [COMMON] |
| `/api/v1/users/change-password` | POST | 修改密码 | [COMMON] |
| `/api/v1/users/{id}/permissions` | GET | 获取用户权限 | [ADMIN] |
| `/api/v1/users/permissions` | PUT | 更新用户权限 | [ADMIN] |

### 3. 巡检区域接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/areas` | GET | 获取区域列表 | [COMMON] |
| `/api/v1/areas/{id}` | GET | 获取区域详情 | [COMMON] |
| `/api/v1/areas` | POST | 创建区域 | [ADMIN] |
| `/api/v1/areas/{id}` | PUT | 更新区域 | [ADMIN] |
| `/api/v1/areas/{id}` | DELETE | 删除区域 | [ADMIN] |
| `/api/v1/areas/{id}/qrcode` | GET | 获取区域二维码 | [COMMON] |
| `/api/v1/areas/{id}/qrcode` | POST | 生成区域二维码 | [ADMIN] |

### 4. 巡检记录接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/records` | GET | 获取巡检记录列表 | [COMMON] |
| `/api/v1/records/{id}` | GET | 获取巡检记录详情 | [COMMON] |
| `/api/v1/records` | POST | 创建巡检记录 | [APP] |
| `/api/v1/records/{id}` | PUT | 更新巡检记录 | [APP] |
| `/api/v1/records/{id}` | DELETE | 删除巡检记录 | [ADMIN] |
| `/api/v1/records/export` | GET | 导出巡检记录 | [ADMIN] |
| `/api/v1/records/{id}/route` | POST | 提交巡检路径 | [APP] |
| `/api/v1/records/{id}/route` | GET | 获取巡检路径 | [COMMON] |

### 5. 巡检任务接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/tasks` | GET | 获取任务列表 | [COMMON] |
| `/api/v1/tasks/{id}` | GET | 获取任务详情 | [COMMON] |
| `/api/v1/tasks` | POST | 创建任务 | [ADMIN] |
| `/api/v1/tasks/{id}` | PUT | 更新任务 | [ADMIN] |
| `/api/v1/tasks/{id}` | DELETE | 删除任务 | [ADMIN] |
| `/api/v1/tasks/{id}/status` | PUT | 更新任务状态 | [APP] |
| `/api/v1/tasks/my` | GET | 获取我的任务 | [APP] |
| `/api/v1/tasks/schedule` | POST | 批量排期任务 | [ADMIN] |

### 6. 问题管理接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/issues` | GET | 获取问题列表 | [COMMON] |
| `/api/v1/issues/{id}` | GET | 获取问题详情 | [COMMON] |
| `/api/v1/issues` | POST | 创建问题 | [APP] |
| `/api/v1/issues/{id}` | PUT | 更新问题 | [COMMON] |
| `/api/v1/issues/{id}` | DELETE | 删除问题 | [ADMIN] |
| `/api/v1/issues/{id}/process` | POST | 处理问题 | [COMMON] |
| `/api/v1/issues/{id}/close` | POST | 关闭问题 | [COMMON] |
| `/api/v1/issues/export` | GET | 导出问题 | [ADMIN] |
| `/api/v1/issue-processes/{id}/images` | POST | 上传问题处理图片 | [COMMON] |
| `/api/v1/issue-processes/{id}/images` | GET | 获取问题处理图片 | [COMMON] |
| `/api/v1/issue-processes/{id}/images/{imageId}` | DELETE | 删除问题处理图片 | [COMMON] |

### 7. 文件上传接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/files/upload` | POST | 上传文件 | [COMMON] |
| `/api/v1/files/list` | GET | 获取文件列表 | [COMMON] |
| `/api/v1/files/{id}` | DELETE | 删除文件 | [COMMON] |
| `/api/v1/files/{id}` | GET | 获取文件信息 | [COMMON] |

### 8. 系统参数接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/system/params` | GET | 获取所有系统参数 | [ADMIN] |
| `/api/v1/system/params/{key}` | GET | 获取指定系统参数 | [COMMON] |
| `/api/v1/system/params` | POST | 设置系统参数 | [ADMIN] |
| `/api/v1/system/params/{key}` | DELETE | 删除系统参数 | [ADMIN] |

### 9. 统计数据接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/statistics/inspection/daily` | GET | 获取每日巡检统计 | [COMMON] |
| `/api/v1/statistics/inspection/weekly` | GET | 获取每周巡检统计 | [COMMON] |
| `/api/v1/statistics/issues` | GET | 获取问题统计 | [COMMON] |
| `/api/v1/statistics/dashboard` | GET | 获取仪表盘数据 | [ADMIN] |
| `/api/v1/statistics/refresh/{type}` | POST | 刷新统计缓存 | [ADMIN] |

### 10. 消息通知接口

| 接口路径 | 方法 | 用途 | 分类 |
|---------|-----|-----|------|
| `/api/v1/notifications` | GET | 获取通知列表 | [COMMON] |
| `/api/v1/notifications/{id}` | GET | 获取通知详情 | [COMMON] |
| `/api/v1/notifications/unread` | GET | 获取未读通知 | [COMMON] |
| `/api/v1/notifications/{id}/read` | PUT | 标记通知已读 | [COMMON] |
| `/api/v1/notifications/read-all` | PUT | 标记所有通知已读 | [COMMON] |
| `/api/v1/notifications` | POST | 发送通知 | [ADMIN] |

## 数据库扩展影响的接口

根据数据库扩展情况，以下接口需要进行更新：

1. 返回值需要包含新增字段的接口：
   - `/api/v1/users/{id}` - 需包含`avatar`字段
   - `/api/v1/areas/{id}` - 需包含`qr_code_url`字段
   - `/api/v1/records/{id}` - 需包含`route_path`字段
   - `/api/v1/issue-processes/{id}` - 需包含`images`字段

2. 新增接口：
   - `/api/v1/users/{id}/avatar` - 用户头像上传和获取
   - `/api/v1/areas/{id}/qrcode` - 区域二维码生成和获取
   - `/api/v1/records/{id}/route` - 巡检路径提交和获取
   - `/api/v1/issue-processes/{id}/images` - 问题处理图片管理
   - `/api/v1/files/*` - 文件上传管理相关接口
   - `/api/v1/system/params/*` - 系统参数管理相关接口
   - `/api/v1/statistics/*` - 统计数据相关接口 