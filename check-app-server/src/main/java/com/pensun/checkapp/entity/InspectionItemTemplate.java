package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检项目模板实体类
 * 用于配置不同区域类型的巡检项目
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_inspection_item_template")
public class InspectionItemTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 适用区域类型：D-数据中心, E-弱电间
     */
    @TableField("area_type")
    private String areaType;

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
     * 项目类型：boolean-是否, number-数值, text-文本
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 是否必填
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 排序序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 默认值
     */
    @TableField("default_value")
    private String defaultValue;

    /**
     * 验证规则（JSON格式）
     */
    @TableField(exist = false)
    private String validationRules;

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

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 