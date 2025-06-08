package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.IssueProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 问题处理记录Mapper接口
 */
@Mapper
public interface IssueProcessMapper extends BaseMapper<IssueProcess> {

    /**
     * 根据问题ID查询处理记录
     *
     * @param issueId 问题ID
     * @return 处理记录列表
     */
    @Select("SELECT p.*, u.real_name as processor_name " +
            "FROM t_issue_process p " +
            "LEFT JOIN t_user u ON p.processor_id = u.id AND u.deleted = 0 " +
            "WHERE p.issue_id = #{issueId} " +
            "ORDER BY p.process_time ASC")
    List<IssueProcess> selectByIssueId(@Param("issueId") Long issueId);
} 