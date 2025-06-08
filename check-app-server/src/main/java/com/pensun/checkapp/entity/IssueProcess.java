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
     * 操作类型：create-创建，process-处理，close-关闭
     */
    @TableField("action")
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
    @TableField("content")
    private String content;

    /**
     * 图片（JSON数组存储多张图片URL）
     */
    @TableField("images")
    private String images;

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
} 