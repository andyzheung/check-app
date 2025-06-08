package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 区域DTO
 */
@Data
@Accessors(chain = true)
public class AreaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    private Long id;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域描述
     */
    private String description;

    /**
     * 区域地址
     */
    private String address;

    /**
     * 区域类型
     */
    private String type;

    /**
     * 区域状态
     */
    private String status;

    /**
     * 巡检项列表
     */
    private List<String> checkItems;
} 