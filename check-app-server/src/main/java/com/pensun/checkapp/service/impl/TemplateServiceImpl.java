package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.TemplateDTO;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.entity.InspectionTemplate;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.InspectionTemplateMapper;
import com.pensun.checkapp.service.InspectionItemTemplateService;
import com.pensun.checkapp.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl extends ServiceImpl<InspectionTemplateMapper, InspectionTemplate> implements TemplateService {
    private final InspectionTemplateMapper templateMapper;
    private final AreaMapper areaMapper;
    private final InspectionItemTemplateService inspectionItemTemplateService;

    @Override
    public TemplateDTO getTemplateByAreaId(Long areaId) {
        TemplateDTO templateDTO = new TemplateDTO();

        // 获取区域信息
        Area area = areaMapper.selectById(areaId);
        if (area == null) {
            return templateDTO; // 返回空模板
        }

        String areaType = area.getAreaType();
        
        // 如果是新的数据中心或弱电间类型，使用新的模板系统
        if ("D".equals(areaType) || "E".equals(areaType)) {
            List<InspectionItemTemplate> templates = inspectionItemTemplateService.getTemplatesByAreaType(areaType);
            
            // 将新模板转换为旧的DTO格式，保持接口兼容
            List<TemplateDTO.InspectionItemDTO> items = templates.stream()
                .map(this::convertNewTemplateToDTO)
                .collect(Collectors.toList());
            
            // 新的模板系统不区分environment和security，全部放在environmentItems中
            templateDTO.setEnvironmentItems(items);
            templateDTO.setSecurityItems(List.of()); // 空列表
            
            return templateDTO;
        }

        // 原有的模板系统逻辑（A、B、C类型）
        // 查询环境巡检项
        LambdaQueryWrapper<InspectionTemplate> envQuery = new LambdaQueryWrapper<>();
        envQuery.eq(InspectionTemplate::getAreaId, areaId);
        envQuery.eq(InspectionTemplate::getType, "environment");
        envQuery.eq(InspectionTemplate::getDeleted, 0);
        envQuery.orderByAsc(InspectionTemplate::getSort);
        List<InspectionTemplate> envItems = templateMapper.selectList(envQuery);
        templateDTO.setEnvironmentItems(envItems.stream().map(this::convertOldTemplateToDTO).collect(Collectors.toList()));

        // 查询安全巡检项
        LambdaQueryWrapper<InspectionTemplate> secQuery = new LambdaQueryWrapper<>();
        secQuery.eq(InspectionTemplate::getAreaId, areaId);
        secQuery.eq(InspectionTemplate::getType, "security");
        secQuery.eq(InspectionTemplate::getDeleted, 0);
        secQuery.orderByAsc(InspectionTemplate::getSort);
        List<InspectionTemplate> secItems = templateMapper.selectList(secQuery);
        templateDTO.setSecurityItems(secItems.stream().map(this::convertOldTemplateToDTO).collect(Collectors.toList()));

        return templateDTO;
    }

    /**
     * 将新的InspectionItemTemplate转换为DTO
     */
    private TemplateDTO.InspectionItemDTO convertNewTemplateToDTO(InspectionItemTemplate template) {
        TemplateDTO.InspectionItemDTO dto = new TemplateDTO.InspectionItemDTO();
        dto.setId(template.getId());
        dto.setName(template.getItemName());
        dto.setType(template.getItemType()); // boolean, number, text
        dto.setSort(template.getSortOrder());
        return dto;
    }

    /**
     * 将旧的InspectionTemplate转换为DTO（兼容性）
     */
    private TemplateDTO.InspectionItemDTO convertOldTemplateToDTO(InspectionTemplate template) {
        TemplateDTO.InspectionItemDTO dto = new TemplateDTO.InspectionItemDTO();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setType("boolean"); // 旧系统默认为boolean类型
        dto.setSort(template.getSort());
        return dto;
    }
} 