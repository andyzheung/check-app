package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 问题DTO
 */
@Data
@Accessors(chain = true)
public class IssueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    private Long id;

    /**
     * 问题编号
     */
    private String issueNo;

    /**
     * 巡检记录ID
     */
    private Long recordId;

    /**
     * 巡检项ID
     */
    private Long itemId;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题类型：environment-环境问题，security-安全问题，device-设备问题，other-其他
     */
    private String type;

    /**
     * 问题状态：open-未处理，processing-处理中，closed-已关闭
     */
    private String status;

    /**
     * 记录人ID
     */
    private Long recorderId;

    /**
     * 记录人姓名
     */
    private String recorderName;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理记录列表
     */
    private List<IssueProcessDTO> processRecords;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 问题详情
     */
    private String detail;

    /**
     * 处理记录DTO
     */
    @Data
    public static class ProcessRecordDTO {
        /**
         * 操作类型
         */
        private String action;

        /**
         * 处理时间
         */
        private LocalDateTime time;

        /**
         * 处理人
         */
        private String processor;

        /**
         * 处理内容
         */
        private String content;

        /**
         * 图片列表
         */
        private List<String> images;
    }
} 