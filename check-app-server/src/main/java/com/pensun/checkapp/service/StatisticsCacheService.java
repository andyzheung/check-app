package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.entity.StatisticsCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计缓存Service接口
 */
public interface StatisticsCacheService extends IService<StatisticsCache> {
    
    /**
     * 获取缓存数据
     *
     * @param cacheKey 缓存键
     * @return 缓存数据
     */
    String getCacheData(String cacheKey);
    
    /**
     * 设置缓存数据
     *
     * @param cacheKey 缓存键
     * @param cacheData 缓存数据
     * @param cacheDesc 缓存描述
     * @return 是否成功
     */
    boolean setCacheData(String cacheKey, String cacheData, String cacheDesc);
    
    /**
     * 获取每日巡检统计
     *
     * @param date 日期
     * @return 统计数据
     */
    Map<String, Object> getDailyInspectionStatistics(LocalDate date);
    
    /**
     * 获取每周巡检统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getWeeklyInspectionStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取问题统计
     *
     * @return 统计数据
     */
    Map<String, Object> getIssueStatistics();
    
    /**
     * 获取仪表盘数据
     *
     * @return 仪表盘数据
     */
    Map<String, Object> getDashboardData();
    
    /**
     * 刷新每日统计
     *
     * @return 是否成功
     */
    boolean refreshDailyStatistics();
    
    /**
     * 刷新每周统计
     *
     * @return 是否成功
     */
    boolean refreshWeeklyStatistics();
    
    /**
     * 刷新问题统计
     *
     * @return 是否成功
     */
    boolean refreshIssueStatistics();
    
    /**
     * 刷新仪表盘数据
     *
     * @return 是否成功
     */
    boolean refreshDashboardData();
    
    /**
     * 刷新所有统计数据
     *
     * @return 是否成功
     */
    boolean refreshAllStatistics();
    
    /**
     * 设置缓存数据（带过期时间）
     *
     * @param cacheKey 缓存键
     * @param cacheData 缓存数据
     * @param expireTime 过期时间
     * @return 是否成功
     */
    boolean setCacheData(String cacheKey, String cacheData, LocalDateTime expireTime);
    
    /**
     * 清除过期缓存
     *
     * @return 清除的记录数
     */
    int clearExpiredCache();
    
    /**
     * 获取问题趋势数据
     *
     * @param timeRange 时间范围，可选值：week, month, year
     * @return 问题趋势数据
     */
    Map<String, Object> getIssueTrend(String timeRange);
    
    /**
     * 获取区域问题分布
     *
     * @return 区域问题分布数据
     */
    List<Map<String, Object>> getIssueByArea();
    
    /**
     * 获取问题处理人员排名
     *
     * @return 处理人员排名数据
     */
    List<Map<String, Object>> getIssueByHandler();
} 