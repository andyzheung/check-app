package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检记录实体类
 */
@Data
@TableName("t_inspection_record")
public class InspectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 巡检区域ID
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 巡检人员ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 巡检时间
     */
    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    /**
     * 巡检状态：normal-正常，abnormal-异常
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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