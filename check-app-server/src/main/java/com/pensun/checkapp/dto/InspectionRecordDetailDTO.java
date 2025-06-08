package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InspectionRecordDetailDTO {
    /**
     * 记录ID
     */
    private Long id;

    /**
     * 记录编号
     */
    private String recordNo;

    /**
     * 区域信息
     */
    private AreaDTO areaInfo;

    /**
     * 巡检人员信息
     */
    private UserDTO inspectorInfo;

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
     * 环境巡检项列表
     */
    private List<InspectionItemDTO> environmentItems;

    /**
     * 安全巡检项列表
     */
    private List<InspectionItemDTO> securityItems;

    /**
     * 巡检备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

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
         * 巡检项类型
         */
        private String type;

        /**
         * 巡检项状态
         */
        private String status;

        /**
         * 备注
         */
        private String remark;
    }

    @Data
    public static class UserDTO {
        /**
         * 用户ID
         */
        private Long id;

        /**
         * 用户名
         */
        private String username;

        /**
         * 真实姓名
         */
        private String realName;
    }
} 