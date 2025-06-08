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
     * 问题描述
     */
    private String description;

    /**
     * 问题类型
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录人名称
     */
    private String recorderName;

    /**
     * 记录人ID
     */
    private Long recorderId;

    /**
     * 问题状态
     */
    private String status;

    /**
     * 问题详情
     */
    private String detail;

    /**
     * 处理记录
     */
    private List<ProcessRecordDTO> processRecords;

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