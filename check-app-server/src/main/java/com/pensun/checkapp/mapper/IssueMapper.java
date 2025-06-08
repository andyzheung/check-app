package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.entity.Issue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 问题Mapper接口
 */
@Mapper
public interface IssueMapper extends BaseMapper<Issue> {

    /**
     * 分页查询问题
     *
     * @param page       分页参数
     * @param type       问题类型
     * @param recorderId 记录人ID
     * @param status     状态
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 分页数据
     */
    @Select("<script>" +
            "SELECT i.*, u.real_name as recorder_name " +
            "FROM t_issue i " +
            "LEFT JOIN t_user u ON i.recorder_id = u.id AND u.deleted = 0 " +
            "WHERE i.deleted = 0 " +
            "<if test='type != null and type != \"\"'> AND i.type = #{type} </if>" +
            "<if test='recorderId != null'> AND i.recorder_id = #{recorderId} </if>" +
            "<if test='status != null and status != \"\"'> AND i.status = #{status} </if>" +
            "<if test='startDate != null'> AND DATE(i.create_time) &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND DATE(i.create_time) &lt;= #{endDate} </if>" +
            "ORDER BY i.create_time DESC" +
            "</script>")
    IPage<Issue> selectIssuePage(Page<Issue> page,
                                @Param("type") String type,
                                @Param("recorderId") Long recorderId,
                                @Param("status") String status,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    /**
     * 统计总问题数
     *
     * @return 问题数
     */
    @Select("SELECT COUNT(*) FROM t_issue WHERE deleted = 0")
    int countTotalIssues();

    /**
     * 统计本周问题数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 问题数
     */
    @Select("SELECT COUNT(*) FROM t_issue WHERE deleted = 0 AND create_time BETWEEN #{startTime} AND #{endTime}")
    int countWeeklyIssues(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计问题分布
     *
     * @return 问题分布数据
     */
    @Select("SELECT type, COUNT(*) as value FROM t_issue WHERE deleted = 0 GROUP BY type")
    List<Map<String, Object>> countIssueDistribution();

    /**
     * 查询本周问题列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 问题列表
     */
    @Select("SELECT i.*, u.real_name as recorder_name " +
            "FROM t_issue i " +
            "LEFT JOIN t_user u ON i.recorder_id = u.id AND u.deleted = 0 " +
            "WHERE i.deleted = 0 AND i.create_time BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY i.create_time DESC " +
            "LIMIT 10")
    List<Issue> selectWeeklyIssues(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
} 