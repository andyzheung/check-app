package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题实体类
 */
@Data
@TableName("t_issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 问题编号
     */
    @TableField("issue_no")
    private String issueNo;

    /**
     * 巡检记录ID
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 巡检项ID
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 问题描述
     */
    @TableField("description")
    private String description;

    /**
     * 问题类型：environment-环境问题，security-安全问题，device-设备问题，other-其他
     */
    @TableField("type")
    private String type;

    /**
     * 问题状态：open-未处理，processing-处理中，closed-已关闭
     */
    @TableField("status")
    private String status;

    /**
     * 记录人ID
     */
    @TableField("recorder_id")
    private Long recorderId;

    /**
     * 处理人ID
     */
    @TableField("handler_id")
    private Long handlerId;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    @TableField("handle_result")
    private String handleResult;

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