package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 巡检区域实体类
 */
@Data
@TableName("t_area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区域编码
     */
    @TableField("code")
    private String areaCode;

    /**
     * 二维码URL
     */
    @TableField("qr_code_url")
    private String qrCodeUrl;

    /**
     * 区域名称
     */
    @TableField("name")
    private String areaName;

    /**
     * 区域类型
     */
    @TableField("type")
    private String areaType;

    /**
     * 区域状态：active-活跃，inactive-未激活
     */
    @TableField("status")
    private String status;

    /**
     * 区域描述
     */
    @TableField("description")
    private String description;

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