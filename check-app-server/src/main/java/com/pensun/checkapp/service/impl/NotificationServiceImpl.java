package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.NotificationDTO;
import com.pensun.checkapp.entity.Notification;
import com.pensun.checkapp.mapper.NotificationMapper;
import com.pensun.checkapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息通知Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    /**
     * 获取用户的消息通知列表
     */
    @Override
    public PageResult<NotificationDTO> getNotificationList(Long userId, String status, int page, int size) {
        // 创建分页对象
        Page<Notification> pageParam = new Page<>(page, size);
        
        // 查询分页数据
        IPage<Notification> pageResult = baseMapper.selectNotificationPage(pageParam, userId, status);
        
        // 转换为DTO
        List<NotificationDTO> dtoList = pageResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 返回分页结果
        return new PageResult<>(dtoList, pageResult.getTotal(), page, size);
    }

    /**
     * 获取用户未读消息数量
     */
    @Override
    public int getUnreadCount(Long userId) {
        return baseMapper.countUnreadNotifications(userId);
    }

    /**
     * 标记消息为已读
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long userId, Long notificationId) {
        Notification notification = new Notification();
        notification.setStatus("read");
        notification.setUpdateTime(LocalDateTime.now());
        return update(notification, 
            new LambdaQueryWrapper<Notification>()
                .eq(Notification::getId, notificationId)
                .eq(Notification::getUserId, userId));
    }

    /**
     * 创建消息通知
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNotification(Long userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setStatus("unread");
        return save(notification);
    }

    /**
     * 转换为DTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        BeanUtils.copyProperties(notification, dto);
        return dto;
    }
} 