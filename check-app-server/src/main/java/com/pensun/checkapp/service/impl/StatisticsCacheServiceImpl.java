package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.StatisticsCache;
import com.pensun.checkapp.mapper.StatisticsCacheMapper;
import com.pensun.checkapp.service.StatisticsCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 统计数据缓存Service实现类
 */
@Slf4j
@Service
public class StatisticsCacheServiceImpl extends ServiceImpl<StatisticsCacheMapper, StatisticsCache> implements StatisticsCacheService {

    @Override
    public String getCacheData(String cacheKey) {
        if (!StringUtils.hasText(cacheKey)) {
            return null;
        }
        return baseMapper.selectDataByKey(cacheKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCacheData(String cacheKey, String cacheData, LocalDateTime expireTime) {
        if (!StringUtils.hasText(cacheKey) || !StringUtils.hasText(cacheData) || expireTime == null) {
            return false;
        }
        
        try {
            // 查询缓存是否存在
            LambdaQueryWrapper<StatisticsCache> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StatisticsCache::getCacheKey, cacheKey);
            StatisticsCache cache = baseMapper.selectOne(queryWrapper);
            
            if (cache == null) {
                // 新增缓存
                cache = new StatisticsCache();
                cache.setCacheKey(cacheKey);
                cache.setCacheData(cacheData);
                cache.setExpireTime(expireTime);
                cache.setCreateTime(LocalDateTime.now());
                cache.setUpdateTime(LocalDateTime.now());
                return save(cache);
            } else {
                // 更新缓存
                int rows = baseMapper.updateCache(cacheKey, cacheData, expireTime);
                return rows > 0;
            }
        } catch (Exception e) {
            log.error("设置缓存数据失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearExpiredCache() {
        try {
            LambdaQueryWrapper<StatisticsCache> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.lt(StatisticsCache::getExpireTime, LocalDateTime.now());
            return baseMapper.delete(queryWrapper);
        } catch (Exception e) {
            log.error("清除过期缓存失败", e);
            return 0;
        }
    }

    @Override
    public boolean setCacheData(String cacheKey, String cacheData, String cacheDesc) {
        if (!StringUtils.hasText(cacheKey) || !StringUtils.hasText(cacheData)) {
            return false;
        }
        
        try {
            // 设置过期时间为1天后
            LocalDateTime expireTime = LocalDateTime.now().plusDays(1);
            return setCacheData(cacheKey, cacheData, expireTime);
        } catch (Exception e) {
            log.error("设置缓存数据失败", e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getDailyInspectionStatistics(java.time.LocalDate date) {
        // 从缓存获取数据
        String cacheKey = "inspection_count_daily";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析每日巡检统计数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getWeeklyInspectionStatistics(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        // 从缓存获取数据
        String cacheKey = "inspection_count_weekly";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析每周巡检统计数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getIssueStatistics() {
        // 从缓存获取数据
        String cacheKey = "issue_statistics";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析问题统计数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getDashboardData() {
        // 从缓存获取数据
        String cacheKey = "dashboard_statistics";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析仪表盘数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("tasks", new HashMap<String, Integer>() {{
            put("total", 0);
            put("pending", 0);
            put("inProgress", 0);
            put("completed", 0);
        }});
        defaultData.put("records", new HashMap<String, Integer>() {{
            put("total", 0);
            put("thisMonth", 0);
            put("lastMonth", 0);
        }});
        defaultData.put("issues", new HashMap<String, Integer>() {{
            put("total", 0);
            put("open", 0);
            put("processing", 0);
            put("closed", 0);
        }});
        defaultData.put("areas", new HashMap<String, Integer>() {{
            put("total", 0);
            put("active", 0);
            put("inactive", 0);
        }});
        defaultData.put("users", new HashMap<String, Integer>() {{
            put("total", 0);
            put("active", 0);
            put("inactive", 0);
        }});
        
        return defaultData;
    }
    
    @Override
    public boolean refreshDailyStatistics() {
        // TODO: 实现刷新每日统计
        return true;
    }
    
    @Override
    public boolean refreshWeeklyStatistics() {
        // TODO: 实现刷新每周统计
        return true;
    }
    
    @Override
    public boolean refreshIssueStatistics() {
        // TODO: 实现刷新问题统计
        return true;
    }
    
    @Override
    public boolean refreshDashboardData() {
        // TODO: 实现刷新仪表盘数据
        return true;
    }
    
    @Override
    public boolean refreshAllStatistics() {
        boolean result = true;
        result &= refreshDailyStatistics();
        result &= refreshWeeklyStatistics();
        result &= refreshIssueStatistics();
        result &= refreshDashboardData();
        return result;
    }
    
    /**
     * 获取问题趋势数据
     * 
     * @param timeRange 时间范围，可选值：week, month, year
     * @return 问题趋势数据
     */
    @Override
    public Map<String, Object> getIssueTrend(String timeRange) {
        // 从缓存获取数据
        String cacheKey = "issues_trend_" + timeRange;
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析问题趋势数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回默认数据
        Map<String, Object> defaultData = new HashMap<>();
        
        // 根据时间范围返回不同的默认数据
        if ("week".equals(timeRange)) {
            defaultData.put("dates", new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"});
            defaultData.put("counts", new Integer[]{0, 0, 0, 0, 0, 0, 0});
        } else if ("month".equals(timeRange)) {
            defaultData.put("dates", new String[]{"第1周", "第2周", "第3周", "第4周"});
            defaultData.put("counts", new Integer[]{0, 0, 0, 0});
        } else {
            defaultData.put("dates", new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"});
            defaultData.put("counts", new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        }
        
        Map<String, Object> types = new HashMap<>();
        types.put("environment", new Integer[]{0, 0, 0, 0});
        types.put("security", new Integer[]{0, 0, 0, 0});
        types.put("device", new Integer[]{0, 0, 0, 0});
        defaultData.put("types", types);
        
        return defaultData;
    }
    
    /**
     * 获取区域问题分布
     * 
     * @return 区域问题分布数据
     */
    @Override
    public List<Map<String, Object>> getIssueByArea() {
        // 从缓存获取数据
        String cacheKey = "issues_by_area";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                // 使用TypeReference处理泛型转换
                return JSON.parseObject(cacheData, new com.alibaba.fastjson.TypeReference<List<Map<String, Object>>>(){});
            } catch (Exception e) {
                log.error("解析区域问题分布数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        return new ArrayList<>();
    }
    
    /**
     * 获取问题处理人员排名
     * 
     * @return 处理人员排名数据
     */
    @Override
    public List<Map<String, Object>> getIssueByHandler() {
        // 从缓存获取数据
        String cacheKey = "issues_by_handler";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                // 使用TypeReference处理泛型转换
                return JSON.parseObject(cacheData, new com.alibaba.fastjson.TypeReference<List<Map<String, Object>>>(){});
            } catch (Exception e) {
                log.error("解析问题处理人员排名数据失败", e);
            }
        }
        
        // 缓存不存在或解析失败，返回空结果
        return new ArrayList<>();
    }
} 