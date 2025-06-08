package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.entity.InspectionRecord;

import java.time.LocalDate;
import java.util.Map;

/**
 * 巡检记录Service接口
 */
public interface RecordService extends IService<InspectionRecord> {

    /**
     * 分页查询巡检记录列表
     *
     * @param page        页码
     * @param pageSize    每页大小
     * @param areaId      区域ID
     * @param inspectorId 巡检人员ID
     * @param status      状态
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return 分页结果
     */
    PageResult<RecordDTO> getRecordList(int page, int pageSize, Long areaId, Long inspectorId, String status, LocalDate startDate, LocalDate endDate);

    /**
     * 获取巡检记录详情
     *
     * @param id 记录ID
     * @return 记录详情
     */
    RecordDTO getRecordDetail(String id);

    /**
     * 创建巡检记录
     *
     * @param recordDTO 记录DTO
     * @return 是否成功
     */
    boolean createRecord(RecordDTO recordDTO);

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getRecordStats();

    /**
     * 获取周趋势数据
     *
     * @return 周趋势数据
     */
    Map<String, Object> getWeeklyTrend();

    /**
     * 获取巡检人员排名
     *
     * @return 巡检人员排名
     */
    Object getInspectorRanking();

    /**
     * 导出巡检记录
     *
     * @param areaId      区域ID
     * @param inspectorId 巡检人员ID
     * @param status      状态
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return Excel文件字节数组
     */
    byte[] exportRecords(Long areaId, Long inspectorId, String status, LocalDate startDate, LocalDate endDate);
} 