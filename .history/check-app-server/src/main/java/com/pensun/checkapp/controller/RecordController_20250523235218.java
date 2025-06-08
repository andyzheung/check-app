package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/records")
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
        java.time.LocalDate start = null, end = null;
        if (startDate != null && !startDate.isEmpty()) start = java.time.LocalDate.parse(startDate);
        if (endDate != null && !endDate.isEmpty()) end = java.time.LocalDate.parse(endDate);
        com.pensun.checkapp.dto.PageResult<com.pensun.checkapp.dto.RecordDTO> pageResult = recordService.getRecordList(page, size, areaId, inspectorId, status, start, end);
        return com.pensun.checkapp.common.Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return recordService.getById(id);
    }

    @PostMapping
    public Result create(@Valid @RequestBody RecordDTO recordDTO) {
        // 保存巡检记录
        return recordService.create(recordDTO);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RecordDTO recordDTO) {
        return recordService.update(id, recordDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return recordService.delete(id);
    }
} 