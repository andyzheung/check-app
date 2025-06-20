package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.mapper.InspectionItemTemplateMapper;
import com.pensun.checkapp.service.InspectionItemTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionItemTemplateServiceImpl extends ServiceImpl<InspectionItemTemplateMapper, InspectionItemTemplate> implements InspectionItemTemplateService {

    private final InspectionItemTemplateMapper inspectionItemTemplateMapper;

    @Override
    public List<InspectionItemTemplate> getTemplatesByAreaType(String areaType) {
        LambdaQueryWrapper<InspectionItemTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionItemTemplate::getAreaType, areaType)
                    .eq(InspectionItemTemplate::getIsActive, true)
                    .orderByAsc(InspectionItemTemplate::getSortOrder);
        return inspectionItemTemplateMapper.selectList(queryWrapper);
    }
} 