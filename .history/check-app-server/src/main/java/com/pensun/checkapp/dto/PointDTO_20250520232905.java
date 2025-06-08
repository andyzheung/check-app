package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PointDTO {
    private Long id;

    @NotBlank(message = "巡检点名称不能为空")
    private String name;

    @NotBlank(message = "巡检点描述不能为空")
    private String description;

    @NotNull(message = "经度不能为空")
    private Double longitude;

    @NotNull(message = "纬度不能为空")
    private Double latitude;

    private String address;
    private String type;
    private String status;
    private String remark;
} 