package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public Result list(@RequestParam(required = false) String keyword,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer size) {
        return recordService.list(keyword, page, size);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return recordService.getById(id);
    }

    @PostMapping
    public Result create(@Valid @RequestBody RecordDTO recordDTO) {
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