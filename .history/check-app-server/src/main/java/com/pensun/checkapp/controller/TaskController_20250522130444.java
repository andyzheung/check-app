package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TaskDTO;
import com.pensun.checkapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public Result list(@RequestParam(required = false) String keyword,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer size) {
        return taskService.list(keyword, page, size);
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
} 