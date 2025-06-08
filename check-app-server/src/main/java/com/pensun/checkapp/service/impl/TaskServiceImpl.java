package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;
import com.pensun.checkapp.entity.InspectionTask;
import com.pensun.checkapp.mapper.TaskMapper;
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
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
@Slf4j  
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Result list(String keyword, String status, Integer page, Integer size, Long areaId, String priority) {
        LambdaQueryWrapper<InspectionTask> wrapper = new LambdaQueryWrapper<>();
        // 添加状态过滤
        if (StringUtils.hasText(status)) {
            wrapper.eq(InspectionTask::getStatus, status.toUpperCase());
        }
        // 添加关键字搜索 - 只使用name字段
        if (StringUtils.hasText(keyword)) {
            wrapper.like(InspectionTask::getName, keyword);
        }
        // 添加区域过滤
        if (areaId != null) {
            wrapper.eq(InspectionTask::getPointId, areaId); // 使用pointId字段，对应数据库中的area_id
        }
        // 添加优先级过滤 - 由于实体类中没有priority字段，暂时不添加此过滤条件
        // 只查询当前用户的任务
        wrapper.eq(InspectionTask::getInspectorId, SecurityUtils.getCurrentUserId());
        wrapper.orderByDesc(InspectionTask::getCreateTime);

        log.info("执行任务查询，条件: status={}, keyword={}, areaId={}, priority={}, page={}, size={}", 
                status, keyword, areaId, priority, page, size);
        Page<InspectionTask> pageResult = taskMapper.selectPage(new Page<>(page, size), wrapper);
        log.info("查询结果: 总记录数={}, 当前页记录数={}", pageResult.getTotal(), pageResult.getRecords().size());
        
        // 处理返回结果，添加前端需要的字段
        if (pageResult.getRecords() != null && !pageResult.getRecords().isEmpty()) {
            for (InspectionTask task : pageResult.getRecords()) {
                // 确保前端可以通过task_name字段获取到名称
                task.setDescription(""); // 设置空值避免NPE
            }
        }
        
        return Result.success(pageResult);
    }

    @Override
    public Result getTodayStats() {
        Long userId = SecurityUtils.getCurrentUserId();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        log.info("获取今日任务统计 - userId: {}, todayStart: {}, todayEnd: {}", userId, todayStart, todayEnd);

        // 查询今日任务总数
        LambdaQueryWrapper<InspectionTask> totalWrapper = new LambdaQueryWrapper<>();
        totalWrapper.eq(InspectionTask::getInspectorId, userId)
                .between(InspectionTask::getPlanTime, todayStart, todayEnd);
        long totalTasks = taskMapper.selectCount(totalWrapper);

        // 查询已完成任务数
        LambdaQueryWrapper<InspectionTask> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(InspectionTask::getInspectorId, userId)
                .eq(InspectionTask::getStatus, "COMPLETED")
                .between(InspectionTask::getPlanTime, todayStart, todayEnd);
        long completedTasks = taskMapper.selectCount(completedWrapper);

        // 查询待完成任务数
        LambdaQueryWrapper<InspectionTask> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(InspectionTask::getInspectorId, userId)
                .eq(InspectionTask::getStatus, "PENDING")
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
        task.setStatus("PENDING");
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
        if (!"PENDING".equals(task.getStatus())) {
            return Result.error("任务状态不正确");
        }
        task.setStatus("IN_PROGRESS");
        taskMapper.updateById(task);
        return Result.success();
    }
} 