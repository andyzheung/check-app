package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检记录实体类
 */
@Data
@Accessors(chain = true)
@TableName("inspection_record")
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
     * 巡检点ID
     */
    @TableField("point_id")
    private Long pointId;

    /**
     * 巡检结果
     */
    @TableField("result")
    private String result;

    /**
     * 巡检图片
     */
    @TableField("images")
    private String images;

    /**
     * 巡检备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 巡检时间
     */
    @TableField("check_time")
    private LocalDateTime checkTime;

    /**
     * 巡检状态：normal-正常，abnormal-异常
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 