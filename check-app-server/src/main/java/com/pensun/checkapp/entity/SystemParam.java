package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统参数实体类
 */
@Data
@TableName("t_system_param")
public class SystemParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 参数键
     */
    @TableField("param_key")
    private String paramKey;

    /**
     * 参数值
     */
    @TableField("param_value")
    private String paramValue;

    /**
     * 参数描述
     */
    @TableField("param_desc")
    private String paramDesc;

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
} 