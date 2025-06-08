package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.NotificationDTO;
import com.pensun.checkapp.entity.Notification;

/**
 * 消息通知Service接口
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 获取用户的消息通知列表
     *
     * @param userId 用户ID
     * @param status 通知状态
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<NotificationDTO> getNotificationList(Long userId, String status, int page, int size);

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    int getUnreadCount(Long userId);

    /**
     * 标记消息为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long userId, Long notificationId);

    /**
     * 创建消息通知
     *
     * @param notification 通知信息
     * @return 是否成功
     */
    boolean createNotification(Notification notification);
} 