package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.mapper.InspectionItemTemplateMapper;
import com.pensun.checkapp.service.InspectionItemTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检项目模板服务实现类
 */
@Service
public class InspectionItemTemplateServiceImpl extends ServiceImpl<InspectionItemTemplateMapper, InspectionItemTemplate> implements InspectionItemTemplateService {

    @Override
    public List<InspectionItemTemplate> getByAreaType(String areaType) {
        LambdaQueryWrapper<InspectionItemTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionItemTemplate::getAreaType, areaType)
                .orderByAsc(InspectionItemTemplate::getSortOrder);
        return list(queryWrapper);
    }

    @Override
    public List<InspectionItemTemplate> getActiveByAreaType(String areaType) {
        LambdaQueryWrapper<InspectionItemTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionItemTemplate::getAreaType, areaType)
                .eq(InspectionItemTemplate::getIsActive, true)
                .orderByAsc(InspectionItemTemplate::getSortOrder);
        return list(queryWrapper);
    }

    @Override
    public boolean saveBatch(List<InspectionItemTemplate> templates) {
        return super.saveBatch(templates);
    }

    @Override
    public boolean updateActiveStatus(Long id, Boolean isActive) {
        InspectionItemTemplate template = new InspectionItemTemplate();
        template.setId(id);
        template.setIsActive(isActive);
        return updateById(template);
    }
} 