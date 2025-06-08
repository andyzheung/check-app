package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 消息通知Mapper接口
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 分页查询用户的消息通知
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 通知状态
     * @return 分页结果
     */
    @Select("SELECT * FROM t_notification WHERE user_id = #{userId} AND deleted = 0 " +
            "AND (#{status} IS NULL OR status = #{status}) " +
            "ORDER BY create_time DESC")
    IPage<Notification> selectNotificationPage(Page<Notification> page, 
            @Param("userId") Long userId, @Param("status") String status);

    /**
     * 统计用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Select("SELECT COUNT(*) FROM t_notification WHERE user_id = #{userId} " +
            "AND status = 'unread' AND deleted = 0")
    int countUnreadNotifications(@Param("userId") Long userId);
} 