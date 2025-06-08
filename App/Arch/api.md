# 巡检App后端接口文档

## 1. 用户登录
- **URL**: `/api/login`
- **方法**: POST
- **请求参数**:
  - username: string
  - password: string
- **返回**:
  - token: string
  - user: { id, name, dept, role }

## 2. 获取用户信息
- **URL**: `/api/user/profile`
- **方法**: GET
- **Header**: Authorization: Bearer {token}
- **返回**: { id, name, dept, role }

## 3. 扫码获取巡检点信息
- **URL**: `/api/inspection-point/{code}`
- **方法**: GET
- **Header**: Authorization: Bearer {token}
- **返回**: { id, code, name, location, status }

## 4. 获取巡检记录列表
- **URL**: `/api/inspection-records`
- **方法**: GET
- **Header**: Authorization: Bearer {token}
- **参数**: page, size, userId, status, dateRange
- **返回**: [ { id, pointName, time, status, inspector }, ... ]

## 5. 获取巡检记录详情
- **URL**: `/api/inspection-record/{id}`
- **方法**: GET
- **Header**: Authorization: Bearer {token}
- **返回**: { id, pointName, time, status, detail, items, remark }

## 6. 提交巡检表单
- **URL**: `/api/inspection-record`
- **方法**: POST
- **Header**: Authorization: Bearer {token}
- **请求体**: { pointId, items: [...], remark }
- **返回**: { success: true }

## 7. 退出登录
- **URL**: `/api/logout`
- **方法**: POST
- **Header**: Authorization: Bearer {token}
- **返回**: { success: true }

## 巡检记录相关接口

### 获取巡检记录列表
```
GET /api/v1/records

Query Parameters:
- page: 页码（从1开始）
- size: 每页大小
- areaId: 区域ID（可选）
- status: 状态（可选，normal/abnormal）
- startDate: 开始日期（可选，YYYY-MM-DD）
- endDate: 结束日期（可选，YYYY-MM-DD）
- keyword: 搜索关键字（可选）

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "list": [
            {
                "id": "记录ID",
                "recordNo": "记录编号",
                "areaId": "区域ID",
                "areaName": "区域名称",
                "areaCode": "区域编码",
                "status": "巡检状态",
                "inspectorName": "巡检人员",
                "inspectionTime": "巡检时间",
                "remark": "备注"
            }
        ],
        "total": "总记录数",
        "page": "当前页码",
        "size": "每页大小"
    }
}
```

### 获取巡检记录详情
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

### 创建巡检记录
```
POST /api/v1/records

Request Body:
{
    "recordNo": "记录编号",
    "areaId": "区域ID",
    "status": "状态",
    "environmentItems": [
        {
            "name": "巡检项名称",
            "type": "巡检项类型",
            "status": "巡检项状态",
            "remark": "备注"
        }
    ],
    "securityItems": [
        {
            "name": "巡检项名称",
            "type": "巡检项类型",
            "status": "巡检项状态",
            "remark": "备注"
        }
    ],
    "remark": "备注"
}

Response:
{
    "code": 200,
    "message": "success",
    "data": "记录ID"
}
```

### 更新巡检记录
```
PUT /api/v1/records/{id}

Request Body:
{
    "status": "状态",
    "environmentItems": [...],
    "securityItems": [...],
    "remark": "备注"
}

Response:
{
    "code": 200,
    "message": "success"
}
```

### 删除巡检记录
```
DELETE /api/v1/records/{id}

Response:
{
    "code": 200,
    "message": "success"
}
```

### 导出巡检记录
```
GET /api/v1/records/export

Query Parameters:
- areaId: 区域ID（可选）
- status: 状态（可选）
- startDate: 开始日期（可选）
- endDate: 结束日期（可选）

Response:
二进制文件流（Excel格式）
```

---

> 说明：
> - 所有接口均为RESTful风格，返回JSON格式。
> - 认证采用Bearer Token，登录后需在Header中携带。
> - 具体字段和接口路径可根据实际后端实现微调。 