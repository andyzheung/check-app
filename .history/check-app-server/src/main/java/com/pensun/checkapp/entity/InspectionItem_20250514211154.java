package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检项实体类
 */
@Data
@TableName("t_inspection_item")
public class InspectionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 巡检项ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 巡检记录ID
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 巡检项名称
     */
    @TableField("name")
    private String name;

    /**
     * 巡检项类型：environment-环境巡检项，security-安全巡检项
     */
    @TableField("type")
    private String type;

    /**
     * 巡检项状态：normal-正常，abnormal-异常
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