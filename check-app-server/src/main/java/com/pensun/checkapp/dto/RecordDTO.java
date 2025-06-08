package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡检记录DTO
 */
@Data
@Accessors(chain = true)
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 记录编号
     */
    private String recordNo;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 巡检人员名称
     */
    private String inspectorName;

    /**
     * 巡检人员ID
     */
    private Long inspectorId;

    /**
     * 巡检时间
     */
    private LocalDateTime inspectionTime;

    /**
     * 巡检开始时间
     */
    private LocalDateTime startTime;

    /**
     * 巡检结束时间
     */
    private LocalDateTime endTime;

    /**
     * 巡检状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 环境巡检项
     */
    private List<InspectionItemDTO> environmentItems;

    /**
     * 安全巡检项
     */
    private List<InspectionItemDTO> securityItems;

    /**
     * 设备巡检项
     */
    private List<InspectionItemDTO> deviceItems;

    /**
     * 巡检项DTO
     */
    @Data
    public static class InspectionItemDTO {
        /**
         * 巡检项ID
         */
        private Long id;

        /**
         * 巡检项名称
         */
        private String name;

        /**
         * 巡检项状态
         */
        private String status;

        /**
         * 备注
         */
        private String remark;
    }
} 