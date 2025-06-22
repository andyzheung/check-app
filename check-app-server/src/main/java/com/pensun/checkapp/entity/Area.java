package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 区域实体类
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
     * 区域名称
     */
    @TableField("name")
    private String name;

    /**
     * 区域描述
     */
    @TableField("description")
    private String description;

    /**
     * 区域地址
     */
    @TableField("address")
    private String address;

    /**
     * 区域类型
     */
    @TableField("type")
    private String type;

    /**
     * 区域状态：active-活跃，inactive-未激活
     */
    @TableField("status")
    private String status;
    
    /**
     * 区域二维码URL
     */
    @TableField("qr_code_url")
    private String qrCodeUrl;

    /**
     * 区域类型：A-机房,B-办公区,C-设备区,D-数据中心,E-弱电间
     */
    @TableField("area_type")
    private String areaType;

    /**
     * 模块数量（仅数据中心使用）
     */
    @TableField("module_count")
    private Integer moduleCount;

    /**
     * 区域配置（JSON格式）
     */
    @TableField("config_json")
    private String configJson;

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

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 获取区域名称
     * 
     * @return 区域名称
     */
    public String getAreaName() {
        return this.name;
    }
    
    /**
     * 获取区域类型
     * 
     * @return 区域类型
     */
    public String getAreaType() {
        return this.areaType;
    }
} 