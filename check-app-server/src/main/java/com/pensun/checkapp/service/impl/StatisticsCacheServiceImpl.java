package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.StatisticsCache;
import com.pensun.checkapp.entity.InspectionRecord;
import com.pensun.checkapp.entity.Issue;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.InspectionTask;
import com.pensun.checkapp.mapper.StatisticsCacheMapper;
import com.pensun.checkapp.mapper.InspectionRecordMapper;
import com.pensun.checkapp.mapper.IssueMapper;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.InspectionTaskMapper;
import com.pensun.checkapp.service.StatisticsCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 统计数据缓存Service实现类
 */
@Slf4j
@Service
public class StatisticsCacheServiceImpl extends ServiceImpl<StatisticsCacheMapper, StatisticsCache> implements StatisticsCacheService {

    @Autowired
    private InspectionRecordMapper inspectionRecordMapper;
    
    @Autowired
    private IssueMapper issueMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private AreaMapper areaMapper;
    
    @Autowired
    private InspectionTaskMapper inspectionTaskMapper;

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
        String cacheKey = "inspection_count_daily_" + date.toString();
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                return JSON.parseObject(cacheData, Map.class);
            } catch (Exception e) {
                log.error("解析每日巡检统计数据失败", e);
            }
        }
        
        // 缓存不存在，从数据库统计
        Map<String, Object> statistics = calculateDailyInspectionStatistics(date);
        
        // 将统计结果缓存起来，缓存1天
        try {
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusDays(1));
        } catch (Exception e) {
            log.error("缓存每日巡检统计数据失败", e);
        }
        
        return statistics;
    }
    
    /**
     * 计算每日巡检统计数据
     */
    private Map<String, Object> calculateDailyInspectionStatistics(LocalDate date) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime startTime = date.atStartOfDay();
            LocalDateTime endTime = date.atTime(23, 59, 59);
            
            // 查询当日巡检记录
            LambdaQueryWrapper<InspectionRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(InspectionRecord::getCreateTime, startTime, endTime)
                   .eq(InspectionRecord::getDeleted, 0);
            List<InspectionRecord> records = inspectionRecordMapper.selectList(wrapper);
            
            // 统计总数和正常/异常数量
            int total = records.size();
            int normal = (int) records.stream().filter(r -> "normal".equals(r.getStatus())).count();
            int abnormal = total - normal;
            
            // 按区域统计
            Map<Long, List<InspectionRecord>> recordsByArea = records.stream()
                .collect(Collectors.groupingBy(InspectionRecord::getAreaId));
            
            List<Map<String, Object>> byArea = new ArrayList<>();
            for (Map.Entry<Long, List<InspectionRecord>> entry : recordsByArea.entrySet()) {
                Long areaId = entry.getKey();
                List<InspectionRecord> areaRecords = entry.getValue();
                
                // 获取区域名称
                Area area = areaMapper.selectById(areaId);
                String areaName = area != null ? area.getName() : "未知区域";
                
                Map<String, Object> areaData = new HashMap<>();
                areaData.put("area_id", areaId);
                areaData.put("area_name", areaName);
                areaData.put("count", areaRecords.size());
                byArea.add(areaData);
            }
            
            result.put("date", date.toString());
            result.put("total", total);
            result.put("normal", normal);
            result.put("abnormal", abnormal);
            result.put("by_area", byArea);
            
        } catch (Exception e) {
            log.error("计算每日巡检统计失败", e);
            // 返回默认数据
            result.put("date", date.toString());
            result.put("total", 0);
            result.put("normal", 0);
            result.put("abnormal", 0);
            result.put("by_area", new ArrayList<>());
        }
        
        return result;
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
        
        // 缓存不存在，从数据库统计
        Map<String, Object> statistics = calculateIssueStatistics();
        
        // 将统计结果缓存起来，缓存1天
        try {
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusDays(1));
        } catch (Exception e) {
            log.error("缓存问题统计数据失败", e);
        }
        
        return statistics;
    }
    
    /**
     * 计算问题统计数据
     */
    private Map<String, Object> calculateIssueStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查询所有问题
            LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Issue::getDeleted, 0);
            List<Issue> allIssues = issueMapper.selectList(wrapper);
            
            // 总数统计
            int total = allIssues.size();
            int open = (int) allIssues.stream().filter(i -> "open".equals(i.getStatus()) || "pending".equals(i.getStatus())).count();
            int processing = (int) allIssues.stream().filter(i -> "processing".equals(i.getStatus())).count();
            int closed = (int) allIssues.stream().filter(i -> "closed".equals(i.getStatus())).count();
            
            result.put("total", total);
            result.put("open", open);
            result.put("processing", processing);
            result.put("closed", closed);
            
            // 按类型统计
            Map<String, Long> typeCount = allIssues.stream()
                .collect(Collectors.groupingBy(Issue::getType, Collectors.counting()));
            
            List<Map<String, Object>> byType = new ArrayList<>();
            for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                Map<String, Object> typeData = new HashMap<>();
                typeData.put("type", entry.getKey());
                typeData.put("count", entry.getValue());
                byType.add(typeData);
            }
            result.put("by_type", byType);
            
            log.info("问题统计完成: 总数={}, 开放={}, 处理中={}, 已关闭={}", total, open, processing, closed);
            
        } catch (Exception e) {
            log.error("计算问题统计数据失败", e);
            // 返回默认数据
            result.put("total", 0);
            result.put("open", 0);
            result.put("processing", 0);
            result.put("closed", 0);
            result.put("by_type", new ArrayList<>());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getDashboardData() {
        // 从缓存获取数据
        String cacheKey = "dashboard_statistics";
        String cacheData = getCacheData(cacheKey);
        
        if (StringUtils.hasText(cacheData)) {
            try {
                Map<String, Object> cachedData = JSON.parseObject(cacheData, Map.class);
                log.info("从缓存获取仪表盘数据成功");
                return cachedData;
            } catch (Exception e) {
                log.error("解析仪表盘缓存数据失败", e);
            }
        }
        
        // 缓存不存在，重新计算
        Map<String, Object> statistics = calculateDashboardData();
        
        // 将统计结果缓存起来，缓存1小时
        try {
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusHours(1));
            log.info("仪表盘数据缓存成功");
        } catch (Exception e) {
            log.error("缓存仪表盘数据失败", e);
        }
        
        return statistics;
    }
    
    /**
     * 计算仪表盘数据
     */
    private Map<String, Object> calculateDashboardData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 任务统计
            Map<String, Object> tasks = new HashMap<>();
            LambdaQueryWrapper<InspectionTask> taskWrapper = new LambdaQueryWrapper<>();
            taskWrapper.eq(InspectionTask::getDeleted, 0);
            List<InspectionTask> allTasks = inspectionTaskMapper.selectList(taskWrapper);
            
            tasks.put("total", allTasks.size());
            tasks.put("pending", (int) allTasks.stream().filter(t -> "pending".equals(t.getStatus())).count());
            tasks.put("inProgress", (int) allTasks.stream().filter(t -> "in_progress".equals(t.getStatus())).count());
            tasks.put("completed", (int) allTasks.stream().filter(t -> "completed".equals(t.getStatus())).count());
            
            // 巡检记录统计
            Map<String, Object> records = new HashMap<>();
            LambdaQueryWrapper<InspectionRecord> recordWrapper = new LambdaQueryWrapper<>();
            recordWrapper.eq(InspectionRecord::getDeleted, 0);
            int totalRecords = inspectionRecordMapper.selectCount(recordWrapper).intValue();
            
            // 本月记录
            LocalDateTime thisMonthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            LocalDateTime thisMonthEnd = LocalDate.now().atTime(23, 59, 59);
            LambdaQueryWrapper<InspectionRecord> thisMonthWrapper = new LambdaQueryWrapper<>();
            thisMonthWrapper.between(InspectionRecord::getCreateTime, thisMonthStart, thisMonthEnd)
                           .eq(InspectionRecord::getDeleted, 0);
            int thisMonthRecords = inspectionRecordMapper.selectCount(thisMonthWrapper).intValue();
            
            // 上月记录
            LocalDateTime lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
            LocalDateTime lastMonthEnd = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);
            LambdaQueryWrapper<InspectionRecord> lastMonthWrapper = new LambdaQueryWrapper<>();
            lastMonthWrapper.between(InspectionRecord::getCreateTime, lastMonthStart, lastMonthEnd)
                           .eq(InspectionRecord::getDeleted, 0);
            int lastMonthRecords = inspectionRecordMapper.selectCount(lastMonthWrapper).intValue();
            
            records.put("total", totalRecords);
            records.put("thisMonth", thisMonthRecords);
            records.put("lastMonth", lastMonthRecords);
            
            // 问题统计
            Map<String, Object> issues = new HashMap<>();
            LambdaQueryWrapper<Issue> issueWrapper = new LambdaQueryWrapper<>();
            issueWrapper.eq(Issue::getDeleted, 0);
            List<Issue> allIssues = issueMapper.selectList(issueWrapper);
            
            issues.put("total", allIssues.size());
            issues.put("open", (int) allIssues.stream().filter(i -> "open".equals(i.getStatus())).count());
            issues.put("processing", (int) allIssues.stream().filter(i -> "processing".equals(i.getStatus())).count());
            issues.put("closed", (int) allIssues.stream().filter(i -> "closed".equals(i.getStatus())).count());
            
            // 区域统计
            Map<String, Object> areas = new HashMap<>();
            LambdaQueryWrapper<Area> areaWrapper = new LambdaQueryWrapper<>();
            areaWrapper.eq(Area::getDeleted, 0);
            List<Area> allAreas = areaMapper.selectList(areaWrapper);
            
            areas.put("total", allAreas.size());
            areas.put("active", (int) allAreas.stream().filter(a -> "active".equals(a.getStatus())).count());
            areas.put("inactive", allAreas.size() - (int) allAreas.stream().filter(a -> "active".equals(a.getStatus())).count());
            
            // 用户统计
            Map<String, Object> users = new HashMap<>();
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getDeleted, 0);
            List<User> allUsers = userMapper.selectList(userWrapper);
            
            users.put("total", allUsers.size());
            users.put("active", (int) allUsers.stream().filter(u -> "active".equals(u.getStatus())).count());
            users.put("inactive", allUsers.size() - (int) allUsers.stream().filter(u -> "active".equals(u.getStatus())).count());
            
            result.put("tasks", tasks);
            result.put("records", records);
            result.put("issues", issues);
            result.put("areas", areas);
            result.put("users", users);
            
        } catch (Exception e) {
            log.error("计算仪表盘数据失败", e);
            // 返回默认数据
            result.put("tasks", new HashMap<String, Integer>() {{
                put("total", 0);
                put("pending", 0);
                put("inProgress", 0);
                put("completed", 0);
            }});
            result.put("records", new HashMap<String, Integer>() {{
                put("total", 0);
                put("thisMonth", 0);
                put("lastMonth", 0);
            }});
            result.put("issues", new HashMap<String, Integer>() {{
                put("total", 0);
                put("open", 0);
                put("processing", 0);
                put("closed", 0);
            }});
            result.put("areas", new HashMap<String, Integer>() {{
                put("total", 0);
                put("active", 0);
                put("inactive", 0);
            }});
            result.put("users", new HashMap<String, Integer>() {{
                put("total", 0);
                put("active", 0);
                put("inactive", 0);
            }});
        }
        
        return result;
    }
    
    @Override
    public boolean refreshDailyStatistics() {
        try {
            LocalDate today = LocalDate.now();
            String cacheKey = "inspection_count_daily_" + today.toString();
            
            // 重新计算今日统计数据
            Map<String, Object> statistics = calculateDailyInspectionStatistics(today);
            
            // 更新缓存，缓存到明天
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusDays(1));
            
            log.info("刷新每日统计数据成功: {}", today);
            return true;
        } catch (Exception e) {
            log.error("刷新每日统计数据失败", e);
            return false;
        }
    }
    
    @Override
    public boolean refreshWeeklyStatistics() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate endDate = startDate.plusDays(6);
            
            String cacheKey = "inspection_count_weekly";
            
            // 重新计算本周统计数据
            Map<String, Object> statistics = getWeeklyInspectionStatistics(startDate, endDate);
            
            // 更新缓存，缓存7天
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusDays(7));
            
            log.info("刷新每周统计数据成功: {} 至 {}", startDate, endDate);
            return true;
        } catch (Exception e) {
            log.error("刷新每周统计数据失败", e);
            return false;
        }
    }
    
    @Override
    public boolean refreshIssueStatistics() {
        try {
            String cacheKey = "issue_statistics";
            
            // 重新计算问题统计数据
            Map<String, Object> statistics = calculateIssueStatistics();
            
            // 更新缓存，缓存1天
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusDays(1));
            
            log.info("刷新问题统计数据成功");
            return true;
        } catch (Exception e) {
            log.error("刷新问题统计数据失败", e);
            return false;
        }
    }
    
    @Override
    public boolean refreshDashboardData() {
        try {
            String cacheKey = "dashboard_statistics";
            
            // 重新计算仪表盘数据
            Map<String, Object> statistics = calculateDashboardData();
            
            // 更新缓存，缓存1小时
            setCacheData(cacheKey, JSON.toJSONString(statistics), LocalDateTime.now().plusHours(1));
            
            log.info("刷新仪表盘数据成功");
            return true;
        } catch (Exception e) {
            log.error("刷新仪表盘数据失败", e);
            return false;
        }
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
        
        // 缓存不存在，从数据库统计
        List<Map<String, Object>> areaData = calculateIssueByArea();
        
        // 将统计结果缓存起来，缓存1天
        try {
            setCacheData(cacheKey, JSON.toJSONString(areaData), LocalDateTime.now().plusDays(1));
        } catch (Exception e) {
            log.error("缓存区域问题分布数据失败", e);
        }
        
        return areaData;
    }
    
    /**
     * 计算区域问题分布
     */
    private List<Map<String, Object>> calculateIssueByArea() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            // 查询所有问题
            LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Issue::getDeleted, 0);
            List<Issue> issues = issueMapper.selectList(wrapper);
            
            // 通过recordId获取区域信息并分组
            Map<Long, Integer> areaIssueCount = new HashMap<>();
            
            for (Issue issue : issues) {
                // 通过recordId获取巡检记录，再获取areaId
                if (issue.getRecordId() != null) {
                    InspectionRecord record = inspectionRecordMapper.selectById(issue.getRecordId());
                    if (record != null && record.getAreaId() != null) {
                        areaIssueCount.merge(record.getAreaId(), 1, Integer::sum);
                    }
                }
            }
            
            // 构建结果
            for (Map.Entry<Long, Integer> entry : areaIssueCount.entrySet()) {
                Long areaId = entry.getKey();
                Integer count = entry.getValue();
                
                // 获取区域名称
                Area area = areaMapper.selectById(areaId);
                String areaName = area != null ? area.getName() : "未知区域";
                
                Map<String, Object> areaData = new HashMap<>();
                areaData.put("name", areaName);
                areaData.put("value", count);
                result.add(areaData);
            }
            
        } catch (Exception e) {
            log.error("计算区域问题分布失败", e);
        }
        
        return result;
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
        
        // 缓存不存在，从数据库统计
        List<Map<String, Object>> handlerData = calculateIssueByHandler();
        
        // 将统计结果缓存起来，缓存1天
        try {
            setCacheData(cacheKey, JSON.toJSONString(handlerData), LocalDateTime.now().plusDays(1));
        } catch (Exception e) {
            log.error("缓存问题处理人员排名数据失败", e);
        }
        
        return handlerData;
    }
    
    /**
     * 计算问题处理人员排名
     */
    private List<Map<String, Object>> calculateIssueByHandler() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            // 查询已处理的问题
            LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Issue::getDeleted, 0)
                   .isNotNull(Issue::getHandlerId)
                   .in(Issue::getStatus, "processing", "closed");
            List<Issue> issues = issueMapper.selectList(wrapper);
            
            // 按处理人分组统计
            Map<Long, Long> handlerCount = issues.stream()
                .collect(Collectors.groupingBy(Issue::getHandlerId, Collectors.counting()));
            
            // 构建结果并排序
            for (Map.Entry<Long, Long> entry : handlerCount.entrySet()) {
                Long handlerId = entry.getKey();
                Long count = entry.getValue();
                
                // 获取处理人姓名
                User handler = userMapper.selectById(handlerId);
                String handlerName = handler != null ? handler.getUsername() : "未知用户";
                
                Map<String, Object> handlerData = new HashMap<>();
                handlerData.put("name", handlerName);
                handlerData.put("value", count.intValue());
                result.add(handlerData);
            }
            
            // 按处理数量降序排序
            result.sort((a, b) -> Integer.compare((Integer)b.get("value"), (Integer)a.get("value")));
            
        } catch (Exception e) {
            log.error("计算问题处理人员排名失败", e);
        }
        
        return result;
    }
} 