package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_inspection_point")
public class InspectionPoint {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String code;
    private String description;
    private String address;
    private String status;
    private Long areaId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
} 