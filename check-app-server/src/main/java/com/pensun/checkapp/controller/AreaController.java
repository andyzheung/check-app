package com.pensun.checkapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.dto.ApiResult;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.service.AreaService;
import com.pensun.checkapp.service.InspectionItemTemplateService;
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
    private final InspectionItemTemplateService inspectionItemTemplateService;
    
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
     * 获取区域二维码
     */
    @GetMapping("/{id}/qrcode")
    public Result<String> getQRCode(@PathVariable Long id) {
        return Result.success(areaService.getQRCode(id));
    }
    
    /**
     * 生成区域二维码
     */
    @PostMapping("/{id}/qrcode")
    public Result<String> generateQRCode(@PathVariable Long id) {
        log.info("生成区域二维码: id={}", id);
        return Result.success(areaService.generateQRCode(id));
    }
    
    /**
     * 验证区域二维码
     */
    @PostMapping("/verify")
    public Result<Boolean> verifyQRCode(@RequestBody String qrData) {
        return Result.success(areaService.verifyQRCode(qrData));
    }



    // TODO: 以下方法将在后续Sprint中实现，需要先完善AreaService接口
    /*
    /**
     * 获取区域的巡检项目配置
     */
    /*
    @GetMapping("/{id}/inspection-items")
    public ApiResult<List<InspectionItem>> getAreaInspectionItems(@PathVariable Long id) {
        // 实现逻辑待完善
        return ApiResult.success(inspectionItemService.getActiveByAreaType("datacenter"));
    }
    */
} 