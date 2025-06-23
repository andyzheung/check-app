package com.pensun.checkapp.common;

/**
 * 任务状态常量类
 * 统一管理所有状态字符串，避免硬编码
 */
public class TaskStatus {
    
    // 任务状态
    public static final String PENDING = "pending";
    public static final String PENDING_UPPER = "PENDING";
    public static final String COMPLETED = "completed";
    public static final String COMPLETED_UPPER = "COMPLETED";
    public static final String IN_PROGRESS = "in_progress";
    public static final String CANCELLED = "cancelled";
    
    // 优先级
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_NORMAL = "normal";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_URGENT = "urgent";
    
    // 巡检项状态
    public static final String ITEM_NORMAL = "normal";
    public static final String ITEM_ABNORMAL = "abnormal";
    
    // 问题状态
    public static final String ISSUE_PENDING = "pending";
    public static final String ISSUE_PROCESSING = "processing";
    public static final String ISSUE_COMPLETED = "completed";
    public static final String ISSUE_OPEN = "open";
    public static final String ISSUE_CLOSED = "closed";
    
    // 区域状态
    public static final String AREA_ACTIVE = "active";
    public static final String AREA_INACTIVE = "inactive";
    
    // 通知状态
    public static final String NOTIFICATION_UNREAD = "unread";
    public static final String NOTIFICATION_READ = "read";
    
    // 私有构造函数，防止实例化
    private TaskStatus() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
} 