package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.entity.InspectionItemTemplate;

import java.util.List;

/**
 * 巡检项目模板服务接口
 */
public interface InspectionItemTemplateService extends IService<InspectionItemTemplate> {

    /**
     * 根据区域类型获取巡检项目模板
     * @param areaType 区域类型
     * @return 巡检项目模板列表
     */
    List<InspectionItemTemplate> getByAreaType(String areaType);

    /**
     * 根据区域类型获取启用的巡检项目模板
     * @param areaType 区域类型
     * @return 巡检项目模板列表
     */
    List<InspectionItemTemplate> getActiveByAreaType(String areaType);

    /**
     * 批量保存巡检项目模板
     * @param templates 模板列表
     * @return 是否成功
     */
    boolean saveBatch(List<InspectionItemTemplate> templates);

    /**
     * 更新模板启用状态
     * @param id 模板ID
     * @param isActive 是否启用
     * @return 是否成功
     */
    boolean updateActiveStatus(Long id, Boolean isActive);
} 