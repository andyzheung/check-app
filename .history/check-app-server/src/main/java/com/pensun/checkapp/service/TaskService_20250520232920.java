package com.pensun.checkapp.service;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;

public interface TaskService {
    Result list(String keyword, Integer page, Integer size);
    Result getById(Long id);
    Result create(TaskDTO taskDTO);
    Result update(Long id, TaskDTO taskDTO);
    Result delete(Long id);
    Result submit(Long id);
} 