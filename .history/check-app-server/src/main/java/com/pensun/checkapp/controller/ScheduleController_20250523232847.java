package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.ScheduleDTO;
import com.pensun.checkapp.service.ScheduleService;
import com.pensun.checkapp.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/today")
    public Result<Map<String, Object>> getTodaySchedules() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<ScheduleDTO> schedules = scheduleService.getTodaySchedules(userId);
        int pendingTasks = scheduleService.getTodayPendingTaskCount(userId);
        int completedTasks = scheduleService.getTodayCompletedTaskCount(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("schedules", schedules);
        result.put("pendingTasks", pendingTasks);
        result.put("completedTasks", completedTasks);

        return Result.ok(result);
    }
} 