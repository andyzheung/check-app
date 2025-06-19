package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.InspectionRecordDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检记录详情Mapper接口
 */
@Mapper
public interface InspectionRecordDetailMapper extends BaseMapper<InspectionRecordDetail> {

    /**
     * 根据记录ID查询详情列表
     *
     * @param recordId 记录ID
     * @return 详情列表
     */
    List<InspectionRecordDetail> selectByRecordId(@Param("recordId") Long recordId);

    /**
     * 批量插入详情记录
     *
     * @param detailList 详情列表
     * @return 插入数量
     */
    int batchInsert(@Param("detailList") List<InspectionRecordDetail> detailList);
} 