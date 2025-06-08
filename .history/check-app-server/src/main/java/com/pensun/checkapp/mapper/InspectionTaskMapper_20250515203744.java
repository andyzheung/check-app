package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.InspectionTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检任务Mapper接口
 */
@Mapper
public interface InspectionTaskMapper extends BaseMapper<InspectionTask> {
} 