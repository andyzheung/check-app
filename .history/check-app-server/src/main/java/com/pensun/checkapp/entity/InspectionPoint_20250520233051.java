package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inspection_point")
public class InspectionPoint {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Double longitude;
    private Double latitude;
    private String address;
    private String type;
    private String status;
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
} 