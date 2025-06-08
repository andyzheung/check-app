package com.pensun.checkapp.service;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;

public interface TaskService {
    Result list(String keyword, String status, Integer page, Integer size, Long areaId, String priority);
    Result getById(Long id);
    Result create(TaskDTO taskDTO);
    Result update(Long id, TaskDTO taskDTO);
    Result delete(Long id);
    Result submit(Long id);
    Result getTodayStats();
} 