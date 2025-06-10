package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.IssueDTO;
import com.pensun.checkapp.entity.Issue;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 问题管理Service接口
 */
public interface IssueService extends IService<Issue> {
    
    /**
     * 获取问题列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param status 状态
     * @param type 类型
     * @param recordId 巡检记录ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 问题列表
     */
    Result getIssueList(Integer page, Integer size, String status, String type, Long recordId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取问题详情
     *
     * @param id 问题ID
     * @return 问题详情
     */
    Result<IssueDTO> getIssueDetail(Long id);
    
    /**
     * 创建问题
     *
     * @param issueDTO 问题DTO
     * @return 创建结果
     */
    Result createIssue(IssueDTO issueDTO);
    
    /**
     * 更新问题
     *
     * @param id 问题ID
     * @param issueDTO 问题DTO
     * @return 更新结果
     */
    Result updateIssue(Long id, IssueDTO issueDTO);
    
    /**
     * 删除问题
     *
     * @param id 问题ID
     * @return 删除结果
     */
    Result deleteIssue(Long id);
    
    /**
     * 处理问题
     *
     * @param id 问题ID
     * @param processData 处理数据
     * @return 处理结果
     */
    Result processIssue(Long id, Map<String, Object> processData);
    
    /**
     * 关闭问题
     *
     * @param id 问题ID
     * @param closeData 关闭数据
     * @return 关闭结果
     */
    Result closeIssue(Long id, Map<String, Object> closeData);
    
    /**
     * 导出问题
     *
     * @param outputStream 输出流
     * @param status 状态
     * @param type 类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @throws Exception 导出异常
     */
    void exportIssues(OutputStream outputStream, String status, String type, LocalDate startDate, LocalDate endDate) throws Exception;
    
    /**
     * 获取本周问题列表
     *
     * @return 本周问题列表
     */
    Result<List<IssueDTO>> getWeeklyIssues();
} 