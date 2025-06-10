package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题处理记录实体类
 */
@Data
@Accessors(chain = true)
@TableName("t_issue_process")
public class IssueProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题ID
     */
    @TableField("issue_id")
    private Long issueId;

    /**
     * 处理动作：create-创建，process-处理中，close-关闭
     */
    private String action;

    /**
     * 处理人ID
     */
    @TableField("processor_id")
    private Long processorId;

    /**
     * 处理时间
     */
    @TableField("process_time")
    private LocalDateTime processTime;

    /**
     * 处理内容
     */
    private String content;

    /**
     * 图片（JSON数组存储多张图片URL）
     */
    private String images;

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
} 