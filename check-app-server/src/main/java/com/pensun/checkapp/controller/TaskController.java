package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;
import com.pensun.checkapp.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public Result list(@RequestParam(required = false) String keyword,
                      @RequestParam(required = false) String status,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer size,
                      @RequestParam(required = false) Long areaId,
                      @RequestParam(required = false) String priority) {
        log.info("获取任务列表 - status: {}, page: {}, size: {}, keyword: {}, areaId: {}, priority: {}", 
                status, page, size, keyword, areaId, priority);
        return taskService.list(keyword, status, page, size, areaId, priority);
    }

    @GetMapping("/today/stats")
    public Result getTodayStats() {
        return taskService.getTodayStats();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public Result create(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.create(taskDTO);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        return taskService.update(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return taskService.delete(id);
    }

    @PostMapping("/{id}/submit")
    public Result submit(@PathVariable Long id) {
        return taskService.submit(id);
    }
    
    /**
     * 测试接口，用于验证修改是否生效
     */
    @GetMapping("/test")
    public Result test() {
        return Result.success("TaskController测试接口正常工作");
    }
} 