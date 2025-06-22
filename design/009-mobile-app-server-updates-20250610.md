# 移动应用与服务器更新设计

## 概述

根据数据库扩展的需求，我们需要对Android移动应用（App/web目录）和服务器端（check-app-server）进行相应的更新，以支持新增的功能和数据结构。本文档详细说明了需要进行的修改内容。

## 1. 数据库扩展概述

数据库扩展主要包括以下内容：

1. `t_area`表增加`qr_code_url`字段 - 用于存储区域二维码URL
2. `t_inspection_record`表增加`route_path`字段 - 用于存储巡检路径（JSON格式）
3. `t_issue_process`表增加`images`字段 - 用于存储问题处理的多张图片URL（JSON数组）
4. `t_user`表增加`avatar`字段 - 用于存储用户头像URL
5. 新增`t_statistics_cache`表 - 用于缓存统计数据
6. 新增`t_system_param`表 - 用于存储系统参数
7. 新增`t_file_upload`表 - 用于管理文件上传

## 2. 后端修改 (check-app-server)

### 2.1 实体类修改

- [x] 已完成 `InspectionRecord` 实体类更新，添加 `routePath` 字段
- [x] 已完成 `FileUpload` 实体类创建
- [x] 已完成 `SystemParam` 实体类创建
- [x] 已完成 `StatisticsCache` 实体类创建
- [ ] 需要更新 `Area` 实体类，添加 `qrCodeUrl` 字段
- [ ] 需要更新 `IssueProcess` 实体类，确保 `images` 字段正确定义
- [ ] 需要更新 `User` 实体类，添加 `avatar` 字段

### 2.2 DTO类修改

- [x] 已完成 `FileUploadDTO` 创建
- [ ] 需要更新 `AreaDTO`，添加 `qrCodeUrl` 字段
- [ ] 需要更新 `IssueProcessDTO`，确保 `images` 字段正确定义
- [ ] 需要更新 `UserDTO`，添加 `avatar` 字段
- [ ] 需要更新 `InspectionRecordDTO`，添加 `routePath` 字段

### 2.3 Mapper接口

- [x] 已完成 `FileUploadMapper` 创建
- [x] 已完成 `SystemParamMapper` 创建
- [x] 已完成 `StatisticsCacheMapper` 创建

### 2.4 Service层

- [x] 已完成 `FileUploadService` 接口和实现
- [x] 已完成 `SystemParamService` 接口和实现
- [x] 已完成 `StatisticsCacheService` 接口和实现
- [ ] 需要更新 `AreaService`，支持二维码URL管理
- [ ] 需要更新 `IssueProcessService`，支持多图片管理
- [ ] 需要更新 `UserService`，支持头像管理
- [ ] 需要更新 `InspectionRecordService`，支持巡检路径管理

### 2.5 Controller层

- [x] 已完成 `FileUploadController` 创建
- [x] 已完成 `SystemParamController` 创建
- [ ] 需要创建 `StatisticsController` 用于前端获取统计数据
- [ ] 需要更新 `AreaController`，提供二维码URL相关接口
- [ ] 需要更新 `IssueProcessController`，支持多图片上传和管理
- [ ] 需要更新 `UserController`，支持头像上传和管理
- [ ] 需要更新 `InspectionRecordController`，支持巡检路径记录和查询

### 2.6 工具类

- [ ] 需要创建 `JsonUtils` 工具类，用于处理JSON格式数据
- [ ] 需要创建 `FileUtils` 工具类，用于处理文件上传和管理
- [ ] 需要创建 `QrCodeUtils` 工具类，用于生成和解析二维码

## 3. 前端修改 (App/web)

### 3.1 Android应用修改

#### 3.1.1 Model类

- [ ] 需要更新 `Area` 模型，添加 `qrCodeUrl` 字段
- [ ] 需要更新 `InspectionRecord` 模型，添加 `routePath` 字段
- [ ] 需要更新 `IssueProcess` 模型，支持多图片
- [ ] 需要更新 `User` 模型，添加 `avatar` 字段
- [ ] 需要创建 `FileUpload` 模型

#### 3.1.2 网络请求

- [ ] 需要更新API接口定义，适配新增的字段和接口
- [ ] 需要创建文件上传相关的API接口
- [ ] 需要创建统计数据获取的API接口

#### 3.1.3 UI界面

- [ ] 区域管理页面添加二维码生成和显示功能
- [ ] 巡检记录页面添加路径轨迹显示功能
- [ ] 问题处理页面优化多图片上传和展示
- [ ] 用户信息页面添加头像上传和展示
- [ ] 添加文件管理相关功能界面
- [ ] 添加数据统计和可视化展示界面

### 3.2 Web前端修改

- [ ] 更新用户管理界面，支持头像上传和显示
- [ ] 更新区域管理界面，支持二维码生成和显示
- [ ] 更新巡检记录页面，支持路径轨迹显示
- [ ] 更新问题处理页面，支持多图片上传和展示
- [ ] 添加系统参数配置界面
- [ ] 添加统计数据展示界面
- [ ] 添加文件管理界面

## 4. 接口设计

### 4.1 区域二维码相关接口

```
GET /api/v1/areas/{id}/qrcode - 获取区域二维码
POST /api/v1/areas/{id}/qrcode - 生成区域二维码
```

### 4.2 巡检路径相关接口

```
POST /api/v1/inspection-records/{id}/route - 提交巡检路径
GET /api/v1/inspection-records/{id}/route - 获取巡检路径
```

### 4.3 问题处理多图片相关接口

```
POST /api/v1/issue-processes/{id}/images - 上传问题处理图片
GET /api/v1/issue-processes/{id}/images - 获取问题处理图片
DELETE /api/v1/issue-processes/{id}/images/{imageId} - 删除问题处理图片
```

### 4.4 用户头像相关接口

```
POST /api/v1/users/{id}/avatar - 上传用户头像
GET /api/v1/users/{id}/avatar - 获取用户头像
```

### 4.5 文件上传通用接口

```
POST /api/v1/files/upload - 上传文件
GET /api/v1/files/list - 获取文件列表
DELETE /api/v1/files/{id} - 删除文件
```

### 4.6 系统参数相关接口

```
GET /api/v1/system/params - 获取所有系统参数
GET /api/v1/system/params/{key} - 获取指定系统参数
POST /api/v1/system/params - 设置系统参数
```

### 4.7 统计数据相关接口

```
GET /api/v1/statistics/inspection/daily - 获取每日巡检统计
GET /api/v1/statistics/inspection/weekly - 获取每周巡检统计
GET /api/v1/statistics/issues - 获取问题统计
```

## 5. 实施计划

1. 完成数据库扩展脚本（已完成）
2. 更新后端实体类和DTO（部分已完成）
3. 实现后端服务和接口（部分已完成）
4. 更新Android应用模型类
5. 实现Android应用新功能
6. 更新Web前端界面
7. 系统测试和调优

## 6. 注意事项

1. 数据库变更需要确保向后兼容性，不影响已有数据
2. 文件上传需要考虑安全性和存储空间管理
3. 移动端需要考虑不同屏幕适配和网络状况
4. 统计数据缓存需要设置合理的过期时间，避免数据过时
5. 所有修改都需要编写单元测试和集成测试 