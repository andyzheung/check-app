package com.pensun.checkapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
     * 获取区域列表（分页）
     */
    @GetMapping
    public Result<Page<AreaDTO>> getAllAreas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取区域列表请求 - status: {}, type: {}, keyword: {}, page: {}, size: {}", 
                status, type, keyword, page, size);
        
        // 直接调用service层的新方法，处理过滤和分页
        return areaService.getAllAreas(status, type, keyword, page, size);
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