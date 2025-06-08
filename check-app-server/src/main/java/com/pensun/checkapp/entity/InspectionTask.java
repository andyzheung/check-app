package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检任务实体类
 */
@Data
@TableName("t_inspection_task")
public class InspectionTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_name")
    private String name;
    
    // 数据库中不存在，但实体类中保留
    @TableField(exist = false)
    private String description;
    
    private LocalDateTime planTime;
    
    // 修改为area_id字段
    @TableField("area_id")
    private Long pointId;
    
    private Long inspectorId;
    private String status;
    
    // 数据库中不存在，但实体类中保留
    @TableField(exist = false)
    private String remark;
    
    // 添加数据库中存在但实体类中没有的字段
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
} 