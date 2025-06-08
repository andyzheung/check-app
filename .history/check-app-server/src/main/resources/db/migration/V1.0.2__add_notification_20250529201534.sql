-- 创建消息通知表
CREATE TABLE IF NOT EXISTS t_notification (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content VARCHAR(500) NOT NULL COMMENT '通知内容',
    type VARCHAR(50) NOT NULL COMMENT '通知类型：task-任务通知，system-系统通知，issue-问题通知',
    status VARCHAR(20) NOT NULL DEFAULT 'unread' COMMENT '通知状态：unread-未读，read-已读',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    KEY idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- 插入测试数据
INSERT INTO t_notification (user_id, title, content, type, status) VALUES
(1, '新任务通知', '您有一个新的巡检任务待处理', 'task', 'unread'),
(1, '系统维护通知', '系统将于今晚22:00进行维护升级', 'system', 'unread'),
(2, '问题处理通知', '您提交的问题已被处理', 'issue', 'unread'),
(2, '任务完成提醒', '您的巡检任务已超时，请及时处理', 'task', 'unread'),
(3, '系统更新通知', '系统已更新到最新版本', 'system', 'unread'); 