package com.pensun.checkapp.dto;

import lombok.Data;

/**
 * 区域配置DTO
 */
@Data
public class AreaConfigDTO {
    
    /**
     * 模块数量
     */
    private Integer moduleCount;
    
    /**
     * 配置JSON字符串
     */
    private String configJson;
} 