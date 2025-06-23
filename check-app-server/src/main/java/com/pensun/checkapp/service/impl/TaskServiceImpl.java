package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.common.PageResult;
import com.pensun.checkapp.common.TaskStatus;
import com.pensun.checkapp.dto.TaskDTO;
import com.pensun.checkapp.entity.InspectionTask;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.mapper.TaskMapper;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.service.TaskService;
import com.pensun.checkapp.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
@Slf4j  
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private AreaMapper areaMapper;

    @Override
    public Result list(String keyword, String status, Integer page, Integer size, Long areaId, String priority) {
        Page<InspectionTask> taskPage = new Page<>(page, size);
        
        LambdaQueryWrapper<InspectionTask> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键字搜索
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(InspectionTask::getName, keyword);
        }
        
        // 状态过滤 - 兼容大小写
        if (StringUtils.hasText(status)) {
            if (TaskStatus.PENDING_UPPER.equalsIgnoreCase(status)) {
                queryWrapper.and(wrapper -> wrapper.eq(InspectionTask::getStatus, TaskStatus.PENDING_UPPER)
                        .or().eq(InspectionTask::getStatus, TaskStatus.PENDING));
            } else if (TaskStatus.COMPLETED_UPPER.equalsIgnoreCase(status)) {
                queryWrapper.and(wrapper -> wrapper.eq(InspectionTask::getStatus, TaskStatus.COMPLETED_UPPER)
                        .or().eq(InspectionTask::getStatus, TaskStatus.COMPLETED));
            } else {
                queryWrapper.eq(InspectionTask::getStatus, status);
            }
        }
        
        // 区域过滤
        if (areaId != null) {
            queryWrapper.eq(InspectionTask::getPointId, areaId);
        }
        
        // 优先级过滤 - 暂时跳过，因为实体类中没有priority字段
        // if (StringUtils.hasText(priority)) {
        //     queryWrapper.eq(InspectionTask::getPriority, priority);
        // }
        
        // 只查询当前用户的任务
        Long userId = SecurityUtils.getCurrentUserId();
        queryWrapper.eq(InspectionTask::getInspectorId, userId);
        
        // 按计划时间排序
        queryWrapper.orderByDesc(InspectionTask::getPlanTime);
        
        Page<InspectionTask> result = taskMapper.selectPage(taskPage, queryWrapper);
        
        // 转换为DTO并补充区域信息
        List<Map<String, Object>> records = result.getRecords().stream().map(task -> {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("id", task.getId());
            taskMap.put("taskName", task.getName());
            taskMap.put("name", task.getName()); // 兼容前端字段
            taskMap.put("areaId", task.getPointId());
            taskMap.put("pointId", task.getPointId()); // 兼容前端字段
            taskMap.put("status", task.getStatus());
            taskMap.put("planTime", task.getPlanTime());
            taskMap.put("startTime", task.getPlanTime()); // 兼容前端字段
            taskMap.put("priority", TaskStatus.PRIORITY_NORMAL); // 默认优先级，因为实体类中没有priority字段
            taskMap.put("description", task.getDescription());
            taskMap.put("createTime", task.getCreateTime());
            taskMap.put("updateTime", task.getUpdateTime());
            
            // 补充区域名称
            if (task.getPointId() != null) {
                Area area = areaMapper.selectById(task.getPointId());
                if (area != null) {
                    taskMap.put("areaName", area.getName());
                } else {
                    taskMap.put("areaName", "未知区域");
                }
            }
            
            return taskMap;
        }).collect(Collectors.toList());
        
        PageResult<Map<String, Object>> pageResult = new PageResult<>(
            records, 
            result.getTotal(), 
            page, 
            size
        );
        
        return Result.success(pageResult);
    }
    


    @Override
    public Result getTodayStats() {
        Long userId = SecurityUtils.getCurrentUserId();
        
        // 使用系统默认时区（已在配置文件中设置为GMT+8）
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59, 59);

        log.info("获取今日任务统计 - userId: {}, todayStart: {}, todayEnd: {}", userId, todayStart, todayEnd);

        // 查询今日任务总数
        LambdaQueryWrapper<InspectionTask> totalWrapper = new LambdaQueryWrapper<>();
        totalWrapper.eq(InspectionTask::getInspectorId, userId)
                .between(InspectionTask::getPlanTime, todayStart, todayEnd);
        long totalTasks = taskMapper.selectCount(totalWrapper);

        // 查询已完成任务数 - 兼容大小写
        LambdaQueryWrapper<InspectionTask> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(InspectionTask::getInspectorId, userId)
                .and(wrapper -> wrapper.eq(InspectionTask::getStatus, TaskStatus.COMPLETED_UPPER)
                        .or().eq(InspectionTask::getStatus, TaskStatus.COMPLETED))
                .between(InspectionTask::getPlanTime, todayStart, todayEnd);
        long completedTasks = taskMapper.selectCount(completedWrapper);

        // 查询待完成任务数 - 兼容大小写
        LambdaQueryWrapper<InspectionTask> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(InspectionTask::getInspectorId, userId)
                .and(wrapper -> wrapper.eq(InspectionTask::getStatus, TaskStatus.PENDING_UPPER)
                        .or().eq(InspectionTask::getStatus, TaskStatus.PENDING))
                .between(InspectionTask::getPlanTime, todayStart, todayEnd);
        long pendingTasks = taskMapper.selectCount(pendingWrapper);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", totalTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("pendingTasks", pendingTasks);
        log.info("任务统计结果 - total: {}, completed: {}, pending: {}", totalTasks, completedTasks, pendingTasks);
        return Result.success(stats);
    }

    @Override
    public Result getById(Long id) {
        InspectionTask task = taskMapper.selectById(id);
        if (task == null) {
            return Result.error("任务不存在");
        }
        return Result.success(task);
    }

    @Override
    @Transactional
    public Result create(TaskDTO taskDTO) {
        InspectionTask task = new InspectionTask();
        BeanUtils.copyProperties(taskDTO, task);
        task.setStatus(TaskStatus.PENDING_UPPER);
        taskMapper.insert(task);
        return Result.success();
    }

    @Override
    @Transactional
    public Result update(Long id, TaskDTO taskDTO) {
        InspectionTask task = taskMapper.selectById(id);
        if (task == null) {
            return Result.error("任务不存在");
        }
        BeanUtils.copyProperties(taskDTO, task);
        taskMapper.updateById(task);
        return Result.success();
    }

    @Override
    @Transactional
    public Result delete(Long id) {
        taskMapper.deleteById(id);
        return Result.success();
    }

    @Override
    @Transactional
    public Result submit(Long id) {
        InspectionTask task = taskMapper.selectById(id);
        if (task == null) {
            return Result.error("任务不存在");
        }
        if (!TaskStatus.PENDING_UPPER.equals(task.getStatus())) {
            return Result.error("任务状态不正确");
        }
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskMapper.updateById(task);
        return Result.success();
    }
} 