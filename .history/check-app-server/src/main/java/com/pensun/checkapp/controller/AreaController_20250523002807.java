package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/{code}")
    public Result<Area> getAreaByCode(@PathVariable String code) {
        Area area = areaService.getByCode(code);
        if (area == null) {
            return Result.failed("未找到区域");
        }
        return Result.success(area);
    }
} 