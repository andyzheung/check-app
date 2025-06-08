package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.entity.InspectionSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<InspectionSchedule> {
    
    @Select("<script>" +
            "SELECT s.*, a.name as area_name, u.real_name as user_name " +
            "FROM t_inspection_schedule s " +
            "LEFT JOIN t_area a ON s.area_id = a.id AND a.deleted = 0 " +
            "LEFT JOIN t_user u ON s.user_id = u.id AND u.deleted = 0 " +
            "WHERE s.deleted = 0 " +
            "<if test='areaId != null'> AND s.area_id = #{areaId} </if>" +
            "<if test='userId != null'> AND s.user_id = #{userId} </if>" +
            "<if test='startDate != null'> AND s.schedule_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND s.schedule_date &lt;= #{endDate} </if>" +
            "ORDER BY s.schedule_date DESC, s.shift ASC" +
            "</script>")
    IPage<InspectionSchedule> selectSchedulePage(Page<InspectionSchedule> page,
                                               @Param("areaId") Long areaId,
                                               @Param("userId") Long userId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    @Select("SELECT s.*, a.name as area_name, u.real_name as user_name " +
            "FROM t_inspection_schedule s " +
            "LEFT JOIN t_area a ON s.area_id = a.id AND a.deleted = 0 " +
            "LEFT JOIN t_user u ON s.user_id = u.id AND u.deleted = 0 " +
            "WHERE s.deleted = 0 AND s.schedule_date = #{date} AND s.user_id = #{userId}")
    List<InspectionSchedule> selectTodaySchedules(@Param("date") LocalDate date, @Param("userId") Long userId);
} 