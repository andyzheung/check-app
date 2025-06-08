package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.ScheduleDTO;
import com.pensun.checkapp.entity.InspectionSchedule;
import com.pensun.checkapp.mapper.ScheduleMapper;
import com.pensun.checkapp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, InspectionSchedule> implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleDTO> getTodaySchedules(Long userId) {
        List<InspectionSchedule> schedules = scheduleMapper.selectTodaySchedules(LocalDate.now(), userId);
        return schedules.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public int getTodayPendingTaskCount(Long userId) {
        List<InspectionSchedule> schedules = scheduleMapper.selectTodaySchedules(LocalDate.now(), userId);
        return (int) schedules.stream().filter(s -> "pending".equals(s.getStatus())).count();
    }

    @Override
    public int getTodayCompletedTaskCount(Long userId) {
        List<InspectionSchedule> schedules = scheduleMapper.selectTodaySchedules(LocalDate.now(), userId);
        return (int) schedules.stream().filter(s -> "completed".equals(s.getStatus())).count();
    }

    private ScheduleDTO convertToDTO(InspectionSchedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        return dto;
    }
} 