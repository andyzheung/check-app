package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 巡检问题实体类
 */
@Data
@Accessors(chain = true)
@TableName("t_inspection_issue")
public class InspectionIssue {
    
    /**
     * 问题ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 巡检记录ID
     */
    private Long recordId;
    
    /**
     * 问题类型ID
     */
    private Long typeId;
    
    /**
     * 问题描述
     */
    private String description;
    
    /**
     * 问题图片（JSON数组存储多张图片URL）
     */
    private String images;
    
    /**
     * 问题位置
     */
    private String location;
    
    /**
     * 经度
     */
    private Double longitude;
    
    /**
     * 纬度
     */
    private Double latitude;
    
    /**
     * 问题状态（pending-待处理，processing-处理中，completed-已完成）
     */
    private String status;
    
    /**
     * 处理人ID
     */
    private Long handlerId;
    
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
    
    /**
     * 处理结果
     */
    private String handleResult;
    
    /**
     * 处理图片（JSON数组存储多张图片URL）
     */
    private String handleImages;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 