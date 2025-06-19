package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检项目模板Mapper接口
 */
@Mapper
public interface InspectionItemTemplateMapper extends BaseMapper<InspectionItemTemplate> {

    /**
     * 根据区域类型查询巡检项目模板
     * @param areaType 区域类型
     * @return 巡检项目模板列表
     */
    List<InspectionItemTemplate> selectByAreaType(@Param("areaType") String areaType);

    /**
     * 根据区域类型查询启用的巡检项目模板
     * @param areaType 区域类型
     * @return 启用的巡检项目模板列表
     */
    List<InspectionItemTemplate> selectActiveByAreaType(@Param("areaType") String areaType);
} 