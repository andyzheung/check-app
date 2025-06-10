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
    private Long taskId;
    private Long areaId;
    private Long inspectorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String remark;
    
    /**
     * 巡检路径（JSON格式存储）
     */
    @TableField("route_path")
    private String routePath;
    
    /**
     * 巡检路径数据（JSON格式存储）
     */
    @TableField("route_data")
    private String routeData;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
} 