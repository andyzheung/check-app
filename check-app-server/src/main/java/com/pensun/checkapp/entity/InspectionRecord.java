package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 巡检记录实体类
 */
@Data
@TableName("t_inspection_record")
public class InspectionRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String recordNo;
    private Long areaId;
    private Long inspectorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
} 