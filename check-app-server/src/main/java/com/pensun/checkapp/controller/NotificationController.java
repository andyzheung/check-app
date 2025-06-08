package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.NotificationDTO;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 消息通知Controller
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取消息通知列表
     */
    @GetMapping
    public Result<PageResult<NotificationDTO>> getNotificationList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Result.failed("未获取到当前登录用户信息");
        }
        User user = (User) authentication.getPrincipal();

        // 查询消息列表
        PageResult<NotificationDTO> result = notificationService.getNotificationList(user.getId(), status, page, size);
        return Result.success(result);
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount() {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Result.failed("未获取到当前登录用户信息");
        }
        User user = (User) authentication.getPrincipal();

        // 查询未读消息数量
        int count = notificationService.getUnreadCount(user.getId());
        return Result.success(count);
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/{id}/read")
    public Result<Boolean> markAsRead(@PathVariable Long id) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Result.failed("未获取到当前登录用户信息");
        }
        User user = (User) authentication.getPrincipal();

        // 标记为已读
        boolean success = notificationService.markAsRead(user.getId(), id);
        return success ? Result.success(true) : Result.failed("操作失败");
    }
} 