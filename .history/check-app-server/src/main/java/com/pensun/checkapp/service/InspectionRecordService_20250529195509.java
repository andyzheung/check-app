package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.entity.InspectionRecord;
import com.pensun.checkapp.dto.RecordDTO;

public interface InspectionRecordService extends IService<InspectionRecord> {
    /**
     * 获取巡检记录详情
     *
     * @param id 记录ID
     * @return 巡检记录详情
     */
    RecordDTO getRecordDetail(String id);
} 