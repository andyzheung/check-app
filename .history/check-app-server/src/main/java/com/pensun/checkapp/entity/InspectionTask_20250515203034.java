package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检任务实体类
 */
@Data
@Accessors(chain = true)
@TableName("t_inspection_task")
public class InspectionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 巡检区域ID
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 巡检人员ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 计划巡检时间
     */
    @TableField("plan_time")
    private LocalDateTime planTime;

    /**
     * 任务状态：pending-待执行，processing-执行中，completed-已完成，canceled-已取消
     */
    @TableField("status")
    private String status;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;
} 