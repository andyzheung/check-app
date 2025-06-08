# 巡检记录详情设计

## 1. 功能概述
扩展巡检记录查询功能，添加记录详情查看功能，以便查看每条巡检记录的完整信息。

## 2. 数据模型扩展
不需要新增表，使用现有的表结构：
- t_inspection_record：巡检记录主表
- t_inspection_item：巡检项表
- t_area：区域表
- t_area_type：区域类型表
- t_user：用户表

## 3. API设计

### 3.1 获取巡检记录详情
```
GET /api/v1/records/{id}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "记录ID",
        "recordNo": "记录编号",
        "areaInfo": {
            "id": "区域ID",
            "areaCode": "区域编码",
            "areaName": "区域名称",
            "areaType": "区域类型",
            "areaTypeName": "区域类型名称",
            "status": "区域状态"
        },
        "inspectorInfo": {
            "id": "巡检人员ID",
            "username": "用户名",
            "realName": "真实姓名"
        },
        "startTime": "巡检开始时间",
        "endTime": "巡检结束时间",
        "status": "巡检状态",
        "environmentItems": [
            {
                "id": "巡检项ID",
                "name": "巡检项名称",
                "type": "巡检项类型",
                "status": "巡检项状态",
                "remark": "备注"
            }
        ],
        "securityItems": [
            {
                "id": "巡检项ID",
                "name": "巡检项名称",
                "type": "巡检项类型",
                "status": "巡检项状态",
                "remark": "备注"
            }
        ],
        "remark": "巡检备注",
        "createTime": "创建时间",
        "updateTime": "更新时间"
    }
}
```

## 4. 前端设计

### 4.1 组件结构
```
views/
  ├── Records.vue                # 巡检记录列表页面
  ├── RecordDetail.vue          # 巡检记录详情页面（新增）
  └── components/
      ├── RecordList.vue        # 记录列表组件
      ├── RecordFilter.vue      # 记录筛选组件
      └── RecordInfo.vue        # 记录信息组件（新增）
```

### 4.2 页面布局
巡检记录详情页面包含以下部分：
1. 顶部导航栏（返回按钮、标题）
2. 区域信息卡片
3. 巡检基本信息卡片
4. 环境巡检项列表卡片
5. 安全巡检项列表卡片
6. 备注信息卡片

### 4.3 交互设计
1. 在记录列表页面点击记录项，跳转到详情页面
2. 详情页面通过记录ID加载完整信息
3. 支持返回列表页面
4. 可查看每个巡检项的状态和备注
5. 区分正常和异常状态的显示样式

## 5. 后端设计

### 5.1 代码结构
```
com.pensun.checkapp
  ├── controller
  │   └── InspectionRecordController.java
  ├── service
  │   ├── InspectionRecordService.java
  │   └── impl/
  │       └── InspectionRecordServiceImpl.java
  ├── mapper
  │   └── InspectionRecordMapper.java
  └── dto
      └── InspectionRecordDetailDTO.java
```

### 5.2 实现策略
1. 使用 MyBatis-Plus 的多表关联查询
2. 实现数据传输对象（DTO）进行数据封装
3. 使用 Spring Cache 进行缓存优化
4. 添加适当的日志记录
5. 实现统一的异常处理

## 6. 安全考虑
1. 接口访问权限控制
2. 数据访问权限验证
3. 敏感信息脱敏
4. XSS防护
5. SQL注入防护

## 7. 性能优化
1. 使用缓存减少数据库查询
2. 优化SQL查询语句
3. 按需加载数据
4. 前端组件按需加载
5. 图片资源懒加载

## 8. 测试策略
1. 单元测试
   - 服务层测试
   - 控制器测试
   - DTO转换测试
2. 集成测试
   - API接口测试
   - 数据库操作测试
3. 前端测试
   - 组件渲染测试
   - 用户交互测试
   - 路由跳转测试 