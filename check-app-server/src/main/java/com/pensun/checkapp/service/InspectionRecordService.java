package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.entity.InspectionRecord;
import com.pensun.checkapp.dto.InspectionRecordDetailDTO;
import com.pensun.checkapp.dto.InspectionRecordDTO;

import java.util.List;
import java.util.Map;

/**
 * 注意：这个接口是根据最新需求重新设计的。
 * 它只包含创建记录的核心功能。
 */
public interface InspectionRecordService extends IService<InspectionRecord> {

    /**
     * 创建巡检记录
     * @param recordDTO 巡检记录数据
     * @return 创建后的记录ID
     */
    Long createInspectionRecord(InspectionRecordDTO recordDTO);

    /**
     * 获取巡检记录详情
     *
     * @param id 记录ID
     * @return 巡检记录详情
     */
    InspectionRecordDetailDTO getRecordDetail(Long id);
    
    /**
     * 保存巡检路径数据
     *
     * @param recordId 记录ID
     * @param routeData 路径数据
     * @return 保存结果
     */
    Result<Boolean> saveRouteData(Long recordId, List<Map<String, Object>> routeData);
    
    /**
     * 获取巡检路径数据
     *
     * @param recordId 记录ID
     * @return 路径数据
     */
    List<Map<String, Object>> getRouteData(Long recordId);
} 