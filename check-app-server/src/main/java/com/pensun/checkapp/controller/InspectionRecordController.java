package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.InspectionRecordDTO;
import com.pensun.checkapp.service.InspectionRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/inspection-records")
@RequiredArgsConstructor
public class InspectionRecordController {

    private final InspectionRecordService inspectionRecordService;

    @PostMapping
    public Result<Long> createRecord(@RequestBody InspectionRecordDTO recordDTO) {
        log.info("接收到巡检记录提交通知: {}", recordDTO);
        try {
            Long recordId = inspectionRecordService.createInspectionRecord(recordDTO);
            return Result.success(recordId);
        } catch (Exception e) {
            log.error("创建巡检记录失败", e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }
} 