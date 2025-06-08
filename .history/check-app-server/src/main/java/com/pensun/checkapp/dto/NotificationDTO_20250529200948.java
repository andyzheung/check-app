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
     * 通知类型
     */
    private String type;
    
    /**
     * 通知状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 