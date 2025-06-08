package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;
import com.pensun.checkapp.entity.InspectionTask;
import com.pensun.checkapp.mapper.TaskMapper;
import com.pensun.checkapp.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Result list(String keyword, Integer page, Integer size) {
        LambdaQueryWrapper<InspectionTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(InspectionTask::getName, keyword)
                    .or()
                    .like(InspectionTask::getDescription, keyword);
        }
        wrapper.orderByDesc(InspectionTask::getCreateTime);

        Page<InspectionTask> pageResult = taskMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
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