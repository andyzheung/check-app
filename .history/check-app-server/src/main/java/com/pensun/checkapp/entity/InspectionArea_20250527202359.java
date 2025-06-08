package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 巡检区域实体类
 */
@Data
@TableName("t_inspection_area")
public class InspectionArea {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 区域编码
     */
    private String areaCode;
    
    /**
     * 二维码图片URL
     */
    private String qrCodeUrl;
    
    /**
     * 区域名称
     */
    private String areaName;
    
    /**
     * 区域类型：A-机房,B-办公区,C-设备区
     */
    private String areaType;
    
    /**
     * 状态：active-启用，inactive-停用
     */
    private String status;
    
    /**
     * 描述
     */
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
} 