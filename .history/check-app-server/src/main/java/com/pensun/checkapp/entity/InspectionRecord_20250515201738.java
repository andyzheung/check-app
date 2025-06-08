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
 * 巡检记录实体类
 */
@Data
@Accessors(chain = true)
@TableName("t_inspection_record")
public class InspectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 巡检任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 巡检人员ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 巡检区域ID
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 巡检开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 巡检结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 巡检状态（0-未完成，1-已完成）
     */
    @TableField("status")
    private Integer status;

    /**
     * 巡检备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 巡检路径（JSON格式存储）
     */
    @TableField("route_path")
    private String routePath;

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