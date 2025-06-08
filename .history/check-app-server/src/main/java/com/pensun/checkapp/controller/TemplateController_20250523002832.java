package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.TemplateDTO;
import com.pensun.checkapp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/{areaId}")
    public Result<TemplateDTO> getTemplateByAreaId(@PathVariable Long areaId) {
        TemplateDTO template = templateService.getTemplateByAreaId(areaId);
        return Result.success(template);
    }
} 