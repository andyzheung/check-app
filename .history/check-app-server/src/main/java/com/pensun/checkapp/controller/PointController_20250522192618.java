package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.PointDTO;
import com.pensun.checkapp.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/points")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping
    public Result list(@RequestParam(required = false) String keyword,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer size) {
        return pointService.list(keyword, page, size);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return pointService.getById(id);
    }

    @PostMapping
    public Result create(@Valid @RequestBody PointDTO pointDTO) {
        return pointService.create(pointDTO);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody PointDTO pointDTO) {
        return pointService.update(id, pointDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return pointService.delete(id);
    }
} 