package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.entity.InspectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 巡检记录Mapper接口
 */
@Mapper
public interface InspectionRecordMapper extends BaseMapper<InspectionRecord> {

    /**
     * 分页查询巡检记录
     *
     * @param page      分页参数
     * @param areaId    区域ID
     * @param inspectorId 巡检人员ID
     * @param status    状态
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 分页数据
     */
    @Select("<script>" +
            "SELECT r.*, a.name as area_name, u.real_name as inspector_name " +
            "FROM t_inspection_record r " +
            "LEFT JOIN t_area a ON r.area_id = a.id AND a.deleted = 0 " +
            "LEFT JOIN t_user u ON r.inspector_id = u.id AND u.deleted = 0 " +
            "WHERE r.deleted = 0 " +
            "<if test='areaId != null'> AND r.area_id = #{areaId} </if>" +
            "<if test='inspectorId != null'> AND r.inspector_id = #{inspectorId} </if>" +
            "<if test='status != null and status != \"\"'> AND r.status = #{status} </if>" +
            "<if test='startDate != null'> AND DATE(r.start_time) &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND DATE(r.start_time) &lt;= #{endDate} </if>" +
            "ORDER BY r.start_time DESC" +
            "</script>")
    IPage<InspectionRecord> selectRecordPage(Page<InspectionRecord> page,
                                            @Param("areaId") Long areaId,
                                            @Param("inspectorId") Long inspectorId,
                                            @Param("status") String status,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    /**
     * 统计总巡检次数
     *
     * @return 巡检次数
     */
    @Select("SELECT COUNT(*) FROM t_inspection_record WHERE deleted = 0")
    int countTotalInspections();

    /**
     * 统计本周巡检次数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 巡检次数
     */
    @Select("SELECT COUNT(*) FROM t_inspection_record WHERE deleted = 0 AND start_time BETWEEN #{startTime} AND #{endTime}")
    int countWeeklyInspections(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
} 