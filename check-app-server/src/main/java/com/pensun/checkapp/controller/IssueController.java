package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.IssueDTO;
import com.pensun.checkapp.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 问题管理Controller
 * 
 * [COMMON] - 该接口可供移动应用和管理后台共用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    /**
     * 获取问题列表
     * [COMMON]
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
    @GetMapping
    public Result getIssueList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long recordId,
            @RequestParam(required = false) Long recorderId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("获取问题列表: page={}, size={}, status={}, type={}, recordId={}, recorderId={}, startDate={}, endDate={}", 
                page, size, status, type, recordId, recorderId, startDate, endDate);
        
        try {
            java.time.LocalDate start = null, end = null;
            if (startDate != null && !startDate.isEmpty()) {
                try {
                    start = java.time.LocalDate.parse(startDate);
                } catch (Exception e) {
                    log.warn("开始日期格式错误: {}", startDate);
                }
            }
            if (endDate != null && !endDate.isEmpty()) {
                try {
                    end = java.time.LocalDate.parse(endDate);
                } catch (Exception e) {
                    log.warn("结束日期格式错误: {}", endDate);
                }
            }
            
            Result result = issueService.getIssueList(page, size, status, type, recordId, start, end);
            
            // 确保返回格式一致
            if (result.getCode() == 200 && result.getData() instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) result.getData();
                Map<String, Object> formattedResult = new java.util.HashMap<>();
                formattedResult.put("list", data.getOrDefault("records", new java.util.ArrayList<>()));
                formattedResult.put("total", data.getOrDefault("total", 0));
                return Result.success(formattedResult);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取问题列表失败", e);
            return Result.failed("获取问题列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取问题详情
     * [COMMON]
     *
     * @param id 问题ID
     * @return 问题详情
     */
    @GetMapping("/{id}")
    public Result<IssueDTO> getIssueDetail(@PathVariable Long id) {
        log.info("获取问题详情: id={}", id);
        return issueService.getIssueDetail(id);
    }

    /**
     * 创建问题
     * [APP]
     *
     * @param issueDTO 问题DTO
     * @return 创建结果
     */
    @PostMapping
    public Result createIssue(@Valid @RequestBody IssueDTO issueDTO) {
        log.info("创建问题: issueDTO={}", issueDTO);
        return issueService.createIssue(issueDTO);
    }

    /**
     * 更新问题
     * [COMMON]
     *
     * @param id 问题ID
     * @param issueDTO 问题DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result updateIssue(@PathVariable Long id, @Valid @RequestBody IssueDTO issueDTO) {
        log.info("更新问题: id={}, issueDTO={}", id, issueDTO);
        return issueService.updateIssue(id, issueDTO);
    }

    /**
     * 删除问题
     * [ADMIN]
     *
     * @param id 问题ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteIssue(@PathVariable Long id) {
        log.info("删除问题: id={}", id);
        return issueService.deleteIssue(id);
    }

    /**
     * 处理问题
     * [COMMON]
     *
     * @param id 问题ID
     * @param processData 处理数据
     * @return 处理结果
     */
    @PostMapping("/{id}/process")
    public Result processIssue(@PathVariable Long id, @RequestBody Map<String, Object> processData) {
        log.info("处理问题: id={}, processData={}", id, processData);
        return issueService.processIssue(id, processData);
    }

    /**
     * 关闭问题
     * [COMMON]
     *
     * @param id 问题ID
     * @param closeData 关闭数据
     * @return 关闭结果
     */
    @PostMapping("/{id}/close")
    public Result closeIssue(@PathVariable Long id, @RequestBody Map<String, Object> closeData) {
        log.info("关闭问题: id={}, closeData={}", id, closeData);
        return issueService.closeIssue(id, closeData);
    }

    /**
     * 导出问题
     * [ADMIN]
     *
     * @param status 状态
     * @param type 类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    @GetMapping("/export")
    public void exportIssues(
            HttpServletResponse response,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("导出问题: status={}, type={}, startDate={}, endDate={}", 
                status, type, startDate, endDate);
        
        java.time.LocalDate start = null, end = null;
        if (startDate != null && !startDate.isEmpty()) start = java.time.LocalDate.parse(startDate);
        if (endDate != null && !endDate.isEmpty()) end = java.time.LocalDate.parse(endDate);
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("问题列表导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            // 导出数据
            issueService.exportIssues(response.getOutputStream(), status, type, start, end);
        } catch (Exception e) {
            log.error("导出问题失败", e);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("{\"code\":500,\"message\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("返回导出错误信息失败", ex);
            }
        }
    }
    
    /**
     * 获取本周问题列表
     * [ADMIN]
     *
     * @return 本周问题列表
     */
    @GetMapping("/weekly")
    public Result<List<IssueDTO>> getWeeklyIssues() {
        log.info("获取本周问题列表");
        
        try {
            // 获取本周开始和结束日期
            LocalDate now = LocalDate.now();
            LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
            LocalDate endOfWeek = now.with(java.time.DayOfWeek.SUNDAY);
            
            // 调用现有的问题列表接口，传入本周日期范围
            Result<Map<String, Object>> result = getIssueList(
                1, 10, null, null, null, null,
                startOfWeek.toString(), endOfWeek.toString()
            );
            
            if (result.getCode() == 200 && result.getData() instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) result.getData();
                List<IssueDTO> issues = (List<IssueDTO>) data.getOrDefault("list", new java.util.ArrayList<>());
                return Result.success(issues);
            }
            
            return Result.success(new java.util.ArrayList<>());
        } catch (Exception e) {
            log.error("获取本周问题列表失败", e);
            return Result.failed("获取本周问题列表失败: " + e.getMessage());
        }
    }
} 