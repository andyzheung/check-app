package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.service.InspectionItemTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inspection-item-templates")
@RequiredArgsConstructor
public class InspectionItemTemplateController {

    private final InspectionItemTemplateService inspectionItemTemplateService;

    @GetMapping
    public Result<List<InspectionItemTemplate>> getTemplatesByAreaType(@RequestParam String areaType) {
        List<InspectionItemTemplate> templates = inspectionItemTemplateService.getTemplatesByAreaType(areaType);
        return Result.success(templates);
    }
} 