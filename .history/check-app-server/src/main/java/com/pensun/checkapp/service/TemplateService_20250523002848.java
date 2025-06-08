package com.pensun.checkapp.service;

import com.pensun.checkapp.dto.TemplateDTO;

public interface TemplateService {
    TemplateDTO getTemplateByAreaId(Long areaId);
} 