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
     * 获取巡检人员排名
     * [ADMIN]
     *
     * @return 巡检人员排名数据
     */
    @GetMapping("/inspectors/ranking")
    public Result<List<Map<String, Object>>> getInspectorRanking() {
        log.info("获取巡检人员排名");
        
        // 模拟数据，后续可以从数据库统计
        List<Map<String, Object>> ranking = new java.util.ArrayList<>();
        
        Map<String, Object> inspector1 = new java.util.HashMap<>();
        inspector1.put("name", "张三");
        inspector1.put("inspectionCount", 45);
        inspector1.put("completionRate", 98.5);
        inspector1.put("avgTime", 25.3);
        ranking.add(inspector1);
        
        Map<String, Object> inspector2 = new java.util.HashMap<>();
        inspector2.put("name", "李四");
        inspector2.put("inspectionCount", 42);
        inspector2.put("completionRate", 96.8);
        inspector2.put("avgTime", 28.1);
        ranking.add(inspector2);
        
        Map<String, Object> inspector3 = new java.util.HashMap<>();
        inspector3.put("name", "王五");
        inspector3.put("inspectionCount", 38);
        inspector3.put("completionRate", 94.2);
        inspector3.put("avgTime", 31.5);
        ranking.add(inspector3);
        
        return Result.success(ranking);
    }
    
    /**
     * 获取问题处理人员统计
     * [ADMIN]
     *
     * @return 问题处理人员统计数据
     */
    @GetMapping("/issues/by-handler")
    public Result<List<Map<String, Object>>> getIssueByHandler() {
        log.info("获取问题处理人员统计");
        
        // 模拟数据，后续可以从数据库统计
        List<Map<String, Object>> handlerStats = new java.util.ArrayList<>();
        
        Map<String, Object> handler1 = new java.util.HashMap<>();
        handler1.put("name", "张三");
        handler1.put("processedCount", 12);
        handler1.put("resolvedCount", 10);
        handler1.put("avgProcessTime", 2.5);
        handlerStats.add(handler1);
        
        Map<String, Object> handler2 = new java.util.HashMap<>();
        handler2.put("name", "李四");
        handler2.put("processedCount", 8);
        handler2.put("resolvedCount", 7);
        handler2.put("avgProcessTime", 3.2);
        handlerStats.add(handler2);
        
        return Result.success(handlerStats);
    }
} 