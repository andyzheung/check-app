package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统参数DTO
 */
@Data
public class SystemParamDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数键
     */
    @NotBlank(message = "参数键不能为空")
    private String paramKey;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数描述
     */
    private String paramDesc;
} 