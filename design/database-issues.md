# 巡检App数据库问题分析

## 1. 错误信息分析

### 1.1 错误详情

首页加载时出现以下数据库错误：

```
Error querying database. Cause: java.sql.SQLSyntaxErrorException: Unknown column 'name' in 'field list'
The error may exist in com/pensun/checkapp/mapper/TaskMapper.java (best guess)
The error may involve defaultParameterMap
The error occurred while setting parameters
SQL: SELECT id,name,description,plan_time,point_id,inspector_id,status,remark,create_time,update_time,deleted FROM t_inspection_task WHERE deleted=0 AND (status = ? AND inspector_id = ?) ORDER BY create_time DESC LIMIT ?
Cause: java.sql.SQLSyntaxErrorException: Unknown column 'name' in 'field list'
```

### 1.2 错误原因分析

通过数据库实际查询和代码分析，发现以下问题：

1. **实体类与数据库表结构不一致**：
   - `InspectionTask`实体类中定义了`name`字段
   - 但数据库表`t_inspection_task`中对应的字段是`task_name`
   - 数据库中不存在`description`、`point_id`和`remark`字段

2. **MyBatis-Plus映射问题**：
   - MyBatis-Plus默认按照实体类字段名直接映射到数据库表字段
   - 未使用`@TableField`注解指定正确的字段映射关系

3. **首页数据加载错误**：
   - 首页组件调用`/tasks`接口获取待完成任务
   - 接口内部使用`TaskMapper`查询数据库，导致SQL错误

## 2. 数据库表结构分析

### 2.1 巡检任务表(t_inspection_task)

通过直接连接数据库查询，获取到的实际表结构如下：

```sql
CREATE TABLE `t_inspection_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_name` varchar(100) NOT NULL,
  `area_id` bigint NOT NULL,
  `inspector_id` bigint NOT NULL,
  `plan_time` datetime DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'pending',
  `create_user_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检任务表'
```

### 2.2 实体类定义

实体类中的定义：

```java
@Data
@TableName("t_inspection_task")
public class InspectionTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;  // 应该是 task_name
    private String description;  // 数据库中不存在
    private LocalDateTime planTime;
    private Long pointId;  // 数据库中不存在
    private Long inspectorId;
    private String status;
    private String remark;  // 数据库中不存在

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
```

### 2.3 字段映射不一致

通过数据库查询和代码分析，确认了以下字段映射不一致问题：

| 实体类字段 | 数据库字段 | 是否匹配 | 说明 |
|------------|------------|----------|------|
| id         | id         | ✅ 匹配  | 主键 |
| name       | task_name  | ❌ 不匹配 | 实体类应使用@TableField注解映射 |
| description| 不存在     | ❌ 不匹配 | 数据库中不存在此字段 |
| planTime   | plan_time  | ✅ 匹配(驼峰转换) | 通过驼峰命名转换匹配 |
| pointId    | 不存在     | ❌ 不匹配 | 数据库中不存在此字段，可能应该是area_id |
| inspectorId| inspector_id| ✅ 匹配(驼峰转换) | 通过驼峰命名转换匹配 |
| status     | status     | ✅ 匹配  | 状态字段 |
| remark     | 不存在     | ❌ 不匹配 | 数据库中不存在此字段 |
| createTime | create_time| ✅ 匹配(驼峰转换) | 通过驼峰命名转换匹配 |
| updateTime | update_time| ✅ 匹配(驼峰转换) | 通过驼峰命名转换匹配 |
| deleted    | deleted    | ✅ 匹配  | 逻辑删除字段 |
| 不存在     | area_id    | ❌ 不匹配 | 实体类缺少此字段 |
| 不存在     | create_user_id | ❌ 不匹配 | 实体类缺少此字段 |

## 3. 数据库数据分析

通过数据库查询，查看了`t_inspection_task`表中的实际数据：

```
+----+-----------------+
| id | task_name       |
+----+-----------------+
|  1 | 机房A区日常巡检 |
|  3 | 配电室日常巡检  |
+----+-----------------+
```

这进一步证实了数据库表中使用的是`task_name`字段，而不是实体类中的`name`字段。

同时，`t_area`表的结构也存在类似问题，包含了冗余字段：

```
+-------------+--------------+------+-----+-------------------+---------------+
| Field       | Type         | Null | Key | Default           | Extra         |
+-------------+--------------+------+-----+-------------------+---------------+
| id          | bigint       | NO   | PRI | NULL              | auto_increment|
| area_code   | varchar(8)   | NO   | UNI | NULL              |               |
| qr_code_url | varchar(255) | YES  |     | NULL              |               |
| area_name   | varchar(50)  | NO   |     | NULL              |               |
| area_type   | char(1)      | NO   |     | NULL              |               |
| code        | varchar(50)  | NO   | UNI | NULL              |               |
| name        | varchar(100) | NO   |     | NULL              |               |
| ...         | ...          | ...  | ... | ...               | ...           |
+-------------+--------------+------+-----+-------------------+---------------+
```

这里存在`area_code`和`code`、`area_name`和`name`等重复字段，导致前端需要尝试多种字段名来获取数据。

## 4. 后端API接口分析

### 4.1 TaskController

`TaskController`提供了任务相关的API接口，使用`TaskService`处理业务逻辑：

```java
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public Result list(@RequestParam(required = false) String keyword,
                      @RequestParam(required = false) String status,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer size) {
        return taskService.list(keyword, status, page, size);
    }

    @GetMapping("/today/stats")
    public Result getTodayStats() {
        return taskService.getTodayStats();
    }
    
    // ... 其他方法
}
```

### 4.2 TaskServiceImpl

`TaskServiceImpl`实现了`TaskService`接口，负责任务的查询和处理：

```java
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Result list(String keyword, String status, Integer page, Integer size) {
        LambdaQueryWrapper<InspectionTask> wrapper = new LambdaQueryWrapper<>();
        // 添加状态过滤
        if (StringUtils.hasText(status)) {
            wrapper.eq(InspectionTask::getStatus, status.toUpperCase());
        }
        // 添加关键字搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.like(InspectionTask::getName, keyword)
                    .or()
                    .like(InspectionTask::getDescription, keyword);
        }
        // 只查询当前用户的任务
        wrapper.eq(InspectionTask::getInspectorId, SecurityUtils.getCurrentUserId());
        wrapper.orderByDesc(InspectionTask::getCreateTime);

        Page<InspectionTask> pageResult = taskMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }
    
    // ... 其他方法
}
```

问题出在这里：`wrapper.like(InspectionTask::getName, keyword)`使用了实体类的`name`字段，而数据库中实际是`task_name`字段。

## 5. 前端数据处理分析

### 5.1 前端任务数据处理

前端在`Home.vue`中处理任务数据，已经尝试适配多种可能的字段名：

```javascript
// 获取待完成任务
async function fetchPendingTasks() {
  try {
    const res = await request.get('/tasks', {
      params: {
        status: 'PENDING',
        page: 1,
        size: 3
      }
    })
    console.log('待完成任务API响应:', res)
    if (res.code === 200 || res.code === 0) {
      pendingTasks.value = (res.data?.records || []).map(task => {
        console.log('处理任务数据:', task)
        return {
          id: task.id,
          areaId: task.areaId,
          areaName: task.areaName || task.name || task.task_name,  // 尝试多种字段名
          startTime: task.planTime || task.plan_time,  // 尝试多种字段名
          status: task.status
        }
      })
      console.log('处理后的任务列表:', pendingTasks.value)
    }
  } catch (err) {
    console.error('获取待完成任务失败:', err)
  }
}
```

### 5.2 前端字段适配

前端代码中尝试适配多种可能的字段名：
- `task.areaName || task.name || task.task_name`
- `task.planTime || task.plan_time`

这种方式虽然可以临时解决问题，但应该修复后端返回的数据结构，确保字段名称一致。

## 6. 数据库表结构与实体类不一致的根本原因

通过分析数据库表结构、实体类和代码，可以确认以下几个问题：

1. **数据库设计与实体类设计分离**：
   - 数据库表结构使用`task_name`，而实体类使用`name`
   - 数据库表有`area_id`和`create_user_id`字段，而实体类缺少这些字段
   - 实体类有`description`、`pointId`和`remark`字段，而数据库表中不存在

2. **缺乏统一的命名规范**：
   - 数据库表使用下划线命名法（如`task_name`）
   - 实体类使用驼峰命名法（如`planTime`）
   - 但实体类中的`name`字段没有正确映射到`task_name`

3. **缺少ORM映射注解**：
   - 未使用`@TableField`注解正确映射字段
   - 实体类中存在数据库表中不存在的字段

4. **数据库迁移与代码不同步**：
   - 迁移脚本创建的表结构与实体类定义不一致
   - 可能是因为数据库结构变更后，实体类未及时更新

## 7. 解决方案

### 7.1 修改实体类映射

修改`InspectionTask`实体类，添加正确的字段映射：

```java
@Data
@TableName("t_inspection_task")
public class InspectionTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_name")
    private String name;  // 映射到 task_name 字段
    
    // 添加缺少的字段
    private Long areaId;  // 映射到 area_id
    private Long createUserId;  // 映射到 create_user_id
    
    // 移除数据库中不存在的字段，或添加@TableField(exist = false)注解
    @TableField(exist = false)
    private String description;
    
    @TableField(exist = false)
    private Long pointId;
    
    @TableField(exist = false)
    private String remark;
    
    private LocalDateTime planTime;
    private Long inspectorId;
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
```

### 7.2 使用DTO对象

创建专门的DTO对象处理前端数据传输，避免直接使用实体类：

```java
@Data
public class TaskDTO {
    private Long id;
    private String name;  // 任务名称
    private Long areaId;  // 区域ID
    private String areaName;  // 区域名称
    private LocalDateTime planTime;  // 计划时间
    private Long inspectorId;  // 巡检人ID
    private String inspectorName;  // 巡检人姓名
    private String status;  // 状态
    private String description;  // 描述信息（前端需要）
    private String remark;  // 备注信息（前端需要）
}
```

### 7.3 Service层处理

在Service层处理数据转换，填充前端需要的字段：

```java
@Service
public class TaskServiceImpl implements TaskService {
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private AreaMapper areaMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public Result list(String keyword, String status, Integer page, Integer size) {
        // 查询任务
        LambdaQueryWrapper<InspectionTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) {
            wrapper.eq(InspectionTask::getStatus, status.toUpperCase());
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(InspectionTask::getName, keyword);
            // 注意：不要使用description字段，因为数据库中不存在
        }
        wrapper.eq(InspectionTask::getInspectorId, SecurityUtils.getCurrentUserId());
        wrapper.orderByDesc(InspectionTask::getCreateTime);
        
        Page<InspectionTask> result = taskMapper.selectPage(new Page<>(page, size), wrapper);
        
        // 转换为DTO
        List<TaskDTO> records = result.getRecords().stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setAreaId(task.getAreaId());
            dto.setPlanTime(task.getPlanTime());
            dto.setInspectorId(task.getInspectorId());
            dto.setStatus(task.getStatus());
            
            // 填充区域名称
            Area area = areaMapper.selectById(task.getAreaId());
            if (area != null) {
                dto.setAreaName(area.getName());
            }
            
            // 填充巡检人姓名
            User user = userMapper.selectById(task.getInspectorId());
            if (user != null) {
                dto.setInspectorName(user.getRealName());
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        // 返回分页结果
        Page<TaskDTO> dtoPage = new Page<>(page, size, result.getTotal());
        dtoPage.setRecords(records);
        
        return Result.success(dtoPage);
    }
}
```

## 8. 其他可能的问题

### 8.1 区域表冗余字段

`t_area`表同时存在`area_code`和`code`、`area_name`和`name`等重复字段，这可能是数据库结构演进过程中产生的冗余。需要统一字段命名，避免混淆。

### 8.2 前端适配多字段名

前端代码中尝试适配多种可能的字段名：
```javascript
areaName: task.areaName || task.name || task.task_name,
startTime: task.planTime || task.plan_time,
```

这种方式虽然可以临时解决问题，但应该修复后端返回的数据结构，确保字段名称一致。

## 9. 总结与建议

### 9.1 短期修复

1. **修改实体类映射**：
   - 在`InspectionTask`实体类中添加`@TableField("task_name")`注解
   - 添加缺少的字段（areaId, createUserId）
   - 标记不存在的字段为`@TableField(exist = false)`

2. **修改Service层查询**：
   - 避免使用不存在的字段进行查询
   - 使用DTO对象返回数据给前端

### 9.2 长期改进

1. **统一命名规范**：
   - 实体类字段使用驼峰命名
   - 数据库表字段使用下划线命名
   - 保持命名一致性

2. **完善ORM配置**：
   - 使用`@TableField`显式指定字段映射
   - 使用`@TableName`显式指定表名

3. **数据库重构**：
   - 消除冗余字段（如`t_area`表中的重复字段）
   - 确保数据库表结构与实体类一致

4. **统一API响应格式**：
   - 前后端协商API响应格式
   - 避免前端使用多种可能的字段名

5. **增强测试覆盖**：
   - 添加单元测试验证实体类映射
   - 添加集成测试验证数据库操作

6. **完善文档**：
   - 记录数据库表结构与实体类映射关系
   - 记录API响应格式规范 