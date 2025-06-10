package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.InspectionRecordDetailDTO;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.service.InspectionRecordService;
import com.pensun.checkapp.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private InspectionRecordService inspectionRecordService;

    @GetMapping
    public Result list(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(required = false) Long areaId,
        @RequestParam(required = false) Long inspectorId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        java.time.LocalDate start = null, end = null;
        if (startDate != null && !startDate.isEmpty()) start = java.time.LocalDate.parse(startDate);
        if (endDate != null && !endDate.isEmpty()) end = java.time.LocalDate.parse(endDate);
        com.pensun.checkapp.dto.PageResult<com.pensun.checkapp.dto.RecordDTO> pageResult = recordService.getRecordList(page, size, areaId, inspectorId, status, start, end);
        return com.pensun.checkapp.common.Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<InspectionRecordDetailDTO> getById(@PathVariable Long id) {
        try {
            log.debug("获取巡检记录详情，记录ID：{}", id);
            InspectionRecordDetailDTO detail = inspectionRecordService.getRecordDetail(id);
            if (detail == null) {
                return Result.failed("巡检记录不存在");
            }
            return Result.success(detail);
        } catch (Exception e) {
            log.error("获取巡检记录详情失败", e);
            return Result.failed("获取巡检记录详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    public Result create(@Valid @RequestBody RecordDTO recordDTO) {
        // 保存巡检记录
        return recordService.create(recordDTO);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RecordDTO recordDTO) {
        return recordService.update(id, recordDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return recordService.delete(id);
    }
    
    /**
     * 提交巡检路径
     * [APP]
     *
     * @param id 记录ID
     * @param routeData 路径数据
     * @return 提交结果
     */
    @PostMapping("/{id}/route")
    public Result<Boolean> submitRoute(@PathVariable Long id, @RequestBody List<Map<String, Object>> routeData) {
        log.info("提交巡检路径: recordId={}, dataSize={}", id, routeData.size());
        return inspectionRecordService.saveRouteData(id, routeData);
    }

    /**
     * 获取巡检路径
     * [COMMON]
     *
     * @param id 记录ID
     * @return 路径数据
     */
    @GetMapping("/{id}/route")
    public Result<List<Map<String, Object>>> getRoute(@PathVariable Long id) {
        log.info("获取巡检路径: recordId={}", id);
        List<Map<String, Object>> routeData = inspectionRecordService.getRouteData(id);
        return Result.success(routeData);
    }

    /**
     * 导出巡检记录
     * [ADMIN]
     *
     * @param keyword 关键字
     * @param areaId 区域ID
     * @param inspectorId 巡检人员ID
     * @param status 状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出文件
     */
    @GetMapping("/export")
    public void exportRecords(
            HttpServletResponse response,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) Long inspectorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("导出巡检记录: areaId={}, inspectorId={}, status={}, startDate={}, endDate={}", 
                areaId, inspectorId, status, startDate, endDate);
        
        java.time.LocalDate start = null, end = null;
        if (startDate != null && !startDate.isEmpty()) start = java.time.LocalDate.parse(startDate);
        if (endDate != null && !endDate.isEmpty()) end = java.time.LocalDate.parse(endDate);
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("巡检记录导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            // 导出数据
            recordService.exportRecords(response.getOutputStream(), areaId, inspectorId, status, start, end);
        } catch (Exception e) {
            log.error("导出巡检记录失败", e);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("{\"code\":500,\"message\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("返回导出错误信息失败", ex);
            }
        }
    }
} 