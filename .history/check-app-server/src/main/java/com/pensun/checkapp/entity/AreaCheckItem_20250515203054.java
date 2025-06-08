package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 区域巡检项模板实体类
 */
@Data
@TableName("t_area_check_item")
public class AreaCheckItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区域ID
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 巡检项名称
     */
    @TableField("name")
    private String name;

    /**
     * 巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项
     */
    @TableField("type")
    private String type;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否必填：0-否，1-是
     */
    @TableField("required")
    private Integer required;

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