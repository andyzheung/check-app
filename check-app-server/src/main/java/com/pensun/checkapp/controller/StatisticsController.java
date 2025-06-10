package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.IssueDTO;
import com.pensun.checkapp.service.IssueService;
import com.pensun.checkapp.service.StatisticsCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计数据Controller
 * 
 * [COMMON] - 该接口可供移动应用和管理后台共用，部分接口仅供管理后台使用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsCacheService statisticsCacheService;
    
    @Autowired
    private IssueService issueService;

    /**
     * 获取每日巡检统计
     * [COMMON]
     *
     * @param date 日期，格式：yyyy-MM-dd
     * @return 每日巡检统计数据
     */
    @GetMapping("/inspection/daily")
    public Result<Map<String, Object>> getDailyInspectionStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("获取每日巡检统计: date={}", date);
        date = date == null ? LocalDate.now() : date;
        Map<String, Object> statistics = statisticsCacheService.getDailyInspectionStatistics(date);
        return Result.success(statistics);
    }

    /**
     * 获取每周巡检统计
     * [COMMON]
     *
     * @param startDate 开始日期，格式：yyyy-MM-dd
     * @param endDate 结束日期，格式：yyyy-MM-dd
     * @return 每周巡检统计数据
     */
    @GetMapping("/inspection/weekly")
    public Result<Map<String, Object>> getWeeklyInspectionStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取每周巡检统计: startDate={}, endDate={}", startDate, endDate);
        
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now.minusDays(now.getDayOfWeek().getValue() - 1);
        }
        if (endDate == null) {
            endDate = startDate.plusDays(6);
        }
        
        Map<String, Object> statistics = statisticsCacheService.getWeeklyInspectionStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 获取问题统计
     * [COMMON]
     *
     * @return 问题统计数据
     */
    @GetMapping("/issues")
    public Result<Map<String, Object>> getIssueStatistics() {
        log.info("获取问题统计");
        Map<String, Object> statistics = statisticsCacheService.getIssueStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取仪表盘数据
     * [ADMIN]
     *
     * @return 仪表盘数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        log.info("获取仪表盘数据");
        Map<String, Object> data = statisticsCacheService.getDashboardData();
        return Result.success(data);
    }

    /**
     * 刷新统计缓存
     * [ADMIN]
     *
     * @param type 缓存类型，可选值：daily, weekly, issues, dashboard, all
     * @return 刷新结果
     */
    @PostMapping("/refresh/{type}")
    public Result<Boolean> refreshStatisticsCache(@PathVariable String type) {
        log.info("刷新统计缓存: type={}", type);
        boolean result;
        
        switch (type.toLowerCase()) {
            case "daily":
                result = statisticsCacheService.refreshDailyStatistics();
                break;
            case "weekly":
                result = statisticsCacheService.refreshWeeklyStatistics();
                break;
            case "issues":
                result = statisticsCacheService.refreshIssueStatistics();
                break;
            case "dashboard":
                result = statisticsCacheService.refreshDashboardData();
                break;
            case "all":
                result = statisticsCacheService.refreshAllStatistics();
                break;
            default:
                return Result.failed("不支持的缓存类型: " + type);
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取问题趋势数据
     * [ADMIN]
     *
     * @param timeRange 时间范围，可选值：week, month, year
     * @return 问题趋势数据
     */
    @GetMapping("/issues/trend")
    public Result<Map<String, Object>> getIssueTrend(@RequestParam(defaultValue = "week") String timeRange) {
        log.info("获取问题趋势数据: timeRange={}", timeRange);
        Map<String, Object> trendData = statisticsCacheService.getIssueTrend(timeRange);
        return Result.success(trendData);
    }
    
    /**
     * 获取区域问题分布
     * [ADMIN]
     *
     * @return 区域问题分布数据
     */
    @GetMapping("/issues/by-area")
    public Result<List<Map<String, Object>>> getIssueByArea() {
        log.info("获取区域问题分布");
        List<Map<String, Object>> areaData = statisticsCacheService.getIssueByArea();
        return Result.success(areaData);
    }
    
    /**
     * 获取问题处理人员排名
     * [ADMIN]
     *
     * @return 处理人员排名数据
     */
    @GetMapping("/issues/by-handler")
    public Result<List<Map<String, Object>>> getIssueByHandler() {
        log.info("获取问题处理人员排名");
        List<Map<String, Object>> handlerData = statisticsCacheService.getIssueByHandler();
        return Result.success(handlerData);
    }
} 