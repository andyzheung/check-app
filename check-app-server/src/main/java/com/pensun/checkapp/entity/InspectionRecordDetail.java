package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检记录详情实体类
 */
@Data
@TableName("t_inspection_record_detail")
public class InspectionRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 巡检记录ID
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 巡检项目名称
     */
    @TableField("item_name")
    private String itemName;

    /**
     * 巡检项目编码
     */
    @TableField("item_code")
    private String itemCode;

    /**
     * 项目类型
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 巡检结果值
     */
    @TableField("item_value")
    private String itemValue;

    /**
     * 是否正常
     */
    @TableField("is_normal")
    private Boolean isNormal;

    /**
     * 备注说明
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
} 