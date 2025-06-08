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

    private final NotificationMapper notificationMapper;

    @Override
    public PageResult<NotificationDTO> getNotificationList(Long userId, String status, int page, int size) {
        // 分页查询
        Page<Notification> notificationPage = new Page<>(page, size);
        IPage<Notification> resultPage = notificationMapper.selectNotificationPage(notificationPage, userId, status);

        // 转换为DTO
        List<NotificationDTO> notificationDTOList = resultPage.getRecords().stream().map(notification -> {
            NotificationDTO dto = new NotificationDTO();
            BeanUtils.copyProperties(notification, dto);
            return dto;
        }).collect(Collectors.toList());

        // 返回分页结果
        return PageResult.of(notificationDTOList, resultPage.getTotal());
    }

    @Override
    public int getUnreadCount(Long userId) {
        return notificationMapper.countUnreadNotifications(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long userId, Long notificationId) {
        // 查询通知
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getId, notificationId)
                .eq(Notification::getUserId, userId)
                .eq(Notification::getDeleted, 0);
        Notification notification = notificationMapper.selectOne(queryWrapper);

        if (notification == null) {
            return false;
        }

        // 更新状态
        notification.setStatus("read");
        notification.setUpdateTime(LocalDateTime.now());
        return notificationMapper.updateById(notification) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNotification(Notification notification) {
        notification.setCreateTime(LocalDateTime.now());
        notification.setUpdateTime(LocalDateTime.now());
        notification.setStatus("unread");
        notification.setDeleted(0);
        return save(notification);
    }
} 