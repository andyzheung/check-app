package com.pensun.checkapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息通知DTO
 */
@Data
public class NotificationDTO {
    
    private Long id;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 通知类型：task-任务通知，system-系统通知，issue-问题通知
     */
    private String type;
    
    /**
     * 通知状态：unread-未读，read-已读
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 