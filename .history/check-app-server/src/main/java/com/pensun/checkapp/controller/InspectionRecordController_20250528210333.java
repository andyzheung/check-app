package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.InspectionRecordDetailDTO;
import com.pensun.checkapp.service.InspectionRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class InspectionRecordController {
    private final InspectionRecordService inspectionRecordService;

    /**
     * 获取巡检记录详情
     *
     * @param id 记录ID
     * @return 巡检记录详情
     */
    @GetMapping("/{id}")
    public Result<InspectionRecordDetailDTO> getRecordDetail(@PathVariable Long id) {
        try {
            log.debug("获取巡检记录详情，记录ID：{}", id);
            InspectionRecordDetailDTO detail = inspectionRecordService.getRecordDetail(id);
            if (detail == null) {
                return Result.fail("巡检记录不存在");
            }
            return Result.ok(detail);
        } catch (Exception e) {
            log.error("获取巡检记录详情失败", e);
            return Result.fail("获取巡检记录详情失败: " + e.getMessage());
        }
    }
} 