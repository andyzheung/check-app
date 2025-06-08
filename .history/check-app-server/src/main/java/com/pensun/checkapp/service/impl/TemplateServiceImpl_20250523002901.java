package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.TemplateDTO;
import com.pensun.checkapp.entity.InspectionTemplate;
import com.pensun.checkapp.mapper.InspectionTemplateMapper;
import com.pensun.checkapp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl extends ServiceImpl<InspectionTemplateMapper, InspectionTemplate> implements TemplateService {
    private final InspectionTemplateMapper templateMapper;

    @Override
    public TemplateDTO getTemplateByAreaId(Long areaId) {
        TemplateDTO templateDTO = new TemplateDTO();

        // 查询环境巡检项
        LambdaQueryWrapper<InspectionTemplate> envQuery = new LambdaQueryWrapper<>();
        envQuery.eq(InspectionTemplate::getAreaId, areaId);
        envQuery.eq(InspectionTemplate::getType, "environment");
        envQuery.eq(InspectionTemplate::getDeleted, 0);
        envQuery.orderByAsc(InspectionTemplate::getSort);
        List<InspectionTemplate> envItems = templateMapper.selectList(envQuery);
        templateDTO.setEnvironmentItems(envItems.stream().map(this::convertToDTO).collect(Collectors.toList()));

        // 查询安全巡检项
        LambdaQueryWrapper<InspectionTemplate> secQuery = new LambdaQueryWrapper<>();
        secQuery.eq(InspectionTemplate::getAreaId, areaId);
        secQuery.eq(InspectionTemplate::getType, "security");
        secQuery.eq(InspectionTemplate::getDeleted, 0);
        secQuery.orderByAsc(InspectionTemplate::getSort);
        List<InspectionTemplate> secItems = templateMapper.selectList(secQuery);
        templateDTO.setSecurityItems(secItems.stream().map(this::convertToDTO).collect(Collectors.toList()));

        return templateDTO;
    }

    private TemplateDTO.InspectionItemDTO convertToDTO(InspectionTemplate template) {
        TemplateDTO.InspectionItemDTO dto = new TemplateDTO.InspectionItemDTO();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setType(template.getType());
        dto.setSort(template.getSort());
        return dto;
    }
} 