# 巡检App 扫码与巡检记录功能设计

## 一、功能概述

1. **扫码功能（H5打桩）**
   - H5端通过按钮模拟扫码（打桩），弹窗输入或自动生成"二维码内容"。
   - 扫码后自动跳转到"记录填写"页面，带上扫码内容。

2. **记录填写与提交**
   - 填写巡检内容（如扫码点、备注、图片等），点击提交，调用Spring Boot后端接口保存记录。

3. **记录查询界面**
   - 展示当前用户的巡检记录。
   - 管理员可查看所有人的记录，普通用户只能看自己的。
   - 前端根据用户角色（token中或用户信息接口）判断展示内容。

---

## 二、后端接口设计（Spring Boot）

### 1. 巡检记录相关接口（/api/v1/records）
- `POST /api/v1/records` 新增巡检记录
- `GET /api/v1/records` 分页查询巡检记录（支持按用户过滤）

#### 记录DTO示例
```java
@Data
public class RecordDTO {
    private Long id;
    private Long userId;
    private String username;
    private String pointCode; // 扫码点
    private String remark;
    private String imageUrl;
    private LocalDateTime createTime;
}
```

#### Controller接口示例
```java
@RestController
@RequestMapping("/api/v1/records")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public Result<Void> createRecord(@RequestBody RecordDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        dto.setUserId(userId);
        recordService.createRecord(dto);
        return Result.success();
    }

    @GetMapping
    public Result<List<RecordDTO>> listRecords(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        Long userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        String role = jwtTokenUtil.getRoleFromToken(token.substring(7));
        return Result.success(recordService.listRecords(page, size, userId, role));
    }
}
```

#### Service层
- `createRecord(RecordDTO dto)`
- `listRecords(Integer page, Integer size, Long userId, String role)`

#### 权限控制
- Service层通过token解析用户信息，判断角色，普通用户只查自己，管理员查全部。

---

## 三、前端H5实现（Vue3）

### 1. 扫码打桩
- 在 `Scan.vue` 页面，点击"扫码"按钮弹窗输入内容，确认后跳转到"记录填写"页面（如 `RecordEdit.vue`），带上扫码内容。

### 2. 记录填写与提交
- `RecordEdit.vue` 页面表单，填写内容，点击提交，调用 `/api/v1/records`。

### 3. 记录查询
- `Records.vue` 页面，页面加载时获取当前用户信息（如 `/api/v1/users/current`），判断角色。
- 管理员显示所有记录，普通用户只显示自己的。

---

## 四、数据结构与权限说明

- **RecordDTO**：统一巡检记录数据结构，包含用户、扫码点、备注、图片、时间等。
- **权限控制**：后端根据token解析用户角色，前端仅做展示区分。

---

## 五、开发与扩展建议

- 充分复用现有架构（如Result、JwtTokenUtil、拦截器、用户体系等）。
- 后续可扩展图片上传、记录详情、导出等功能。

## 巡检排班功能设计

### 1. 功能概述
- 支持每周巡检排班计划的制定（选择人员、区域、日期、班次等）
- 支持排班计划的查询、编辑、删除
- 支持按人员、区域、日期筛选排班

### 2. 页面原型
- 管理端新增 `schedule.html` 页面，风格与现有管理端一致
- 主要区域：筛选区、排班表格、操作按钮、新增/编辑弹窗

### 3. 数据库表设计（inspection_schedule）
| 字段名      | 类型         | 说明         |
| ----------- | ------------ | ------------ |
| id          | bigint       | 主键         |
| user_id     | bigint       | 巡检人员ID   |
| area_id     | bigint       | 区域ID       |
| date        | date         | 排班日期     |
| shift       | varchar(16)  | 班次（早/中/晚）|
| remark      | varchar(128) | 备注         |
| create_time | datetime     | 创建时间     |
| update_time | datetime     | 更新时间     |
| deleted     | tinyint      | 逻辑删除     |

### 4. 后端接口设计
- 查询排班计划：`GET /api/v1/schedules?week=2024-06-01&areaId=1&userId=2`
- 新增排班：`POST /api/v1/schedules`，body: `{ userId, areaId, date, shift, remark }`
- 编辑排班：`PUT /api/v1/schedules/{id}`，body: `{ userId, areaId, date, shift, remark }`
- 删除排班：`DELETE /api/v1/schedules/{id}`
- 获取所有区域：`GET /api/v1/areas`
- 获取所有用户：`GET /api/v1/users?role=user`

### 5. 前端页面说明
- schedule.html 见 Admin-UI 目录，风格与 users.html、records.html 保持一致
- 支持表格视图、弹窗增改、筛选、删除等操作
- 具体代码见 Admin-UI/schedule.html 