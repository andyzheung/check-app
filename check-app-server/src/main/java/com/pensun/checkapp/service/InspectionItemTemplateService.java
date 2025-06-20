package com.pensun.checkapp.service;

import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 巡检项目模板服务接口
 */
public interface InspectionItemTemplateService extends IService<InspectionItemTemplate> {

    /**
     * 根据区域类型获取所有启用的巡检项目模板
     * @param areaType 区域类型
     * @return 模板列表
     */
    List<InspectionItemTemplate> getTemplatesByAreaType(String areaType);
} 