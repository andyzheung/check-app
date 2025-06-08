package com.pensun.checkapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域控制器
 */
@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
@Slf4j
public class AreaController {
    
    private final AreaService areaService;
    
    /**
     * 获取所有区域列表
     */
    @GetMapping
    public Result<List<AreaDTO>> getAllAreas(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取区域列表请求 - status: {}, page: {}, size: {}", status, page, size);
        List<AreaDTO> areas = areaService.getAllAreas();
        
        // 简单的内存分页处理
        int total = areas.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        // 防止越界
        if (start >= total) {
            start = 0;
            end = 0;
        }
        
        List<AreaDTO> pagedAreas = areas.subList(start, end);
        
        // 构建分页对象
        Page<AreaDTO> pageResult = new Page<>(page, size, total);
        pageResult.setRecords(pagedAreas);
        
        log.info("返回区域列表 - 总数: {}, 当前页数量: {}", total, pagedAreas.size());
        return Result.success(pageResult);
    }
    
    /**
     * 根据区域编码查询区域信息
     */
    @GetMapping("/code/{areaCode}")
    public Result<AreaDTO> getAreaByCode(@PathVariable String areaCode) {
        return Result.success(areaService.getAreaByCode(areaCode));
    }
    
    /**
     * 生成区域二维码
     */
    @GetMapping("/{id}/qrcode")
    public Result<String> generateQRCode(@PathVariable Long id) {
        return Result.success(areaService.generateQRCode(id));
    }
    
    /**
     * 验证区域二维码
     */
    @PostMapping("/verify")
    public Result<Boolean> verifyQRCode(@RequestBody String qrData) {
        return Result.success(areaService.verifyQRCode(qrData));
    }
} 