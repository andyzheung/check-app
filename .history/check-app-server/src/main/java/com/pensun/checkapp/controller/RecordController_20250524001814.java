package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public Result list(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(required = false) Long areaId,
        @RequestParam(required = false) Long inspectorId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        log.info("查询巡检记录列表: keyword={}, page={}, size={}, areaId={}, inspectorId={}, status={}, startDate={}, endDate={}",
            keyword, page, size, areaId, inspectorId, status, startDate, endDate);
            
        LocalDate start = null, end = null;
        if (startDate != null && !startDate.isEmpty()) {
            start = LocalDate.parse(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            end = LocalDate.parse(endDate);
        }
        
        return Result.success(recordService.getRecordList(page, size, areaId, inspectorId, status, start, end));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("获取巡检记录详情: id={}", id);
        return recordService.getById(id);
    }

    @PostMapping
    public Result create(@Valid @RequestBody RecordDTO recordDTO) {
        log.info("创建巡检记录: recordDTO={}", recordDTO);
        return recordService.create(recordDTO);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RecordDTO recordDTO) {
        log.info("更新巡检记录: id={}, recordDTO={}", id, recordDTO);
        return recordService.update(id, recordDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除巡检记录: id={}", id);
        return recordService.delete(id);
    }
} 