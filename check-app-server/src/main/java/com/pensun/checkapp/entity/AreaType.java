package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 区域类型实体类
 */
@Data
@TableName("t_area_type")
public class AreaType {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 类型编码
     */
    private String typeCode;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 类型描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 