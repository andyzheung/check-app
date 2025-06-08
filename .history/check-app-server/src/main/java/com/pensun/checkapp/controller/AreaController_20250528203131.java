package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域控制器
 */
@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {
    
    private final AreaService areaService;
    
    /**
     * 获取所有区域列表
     */
    @GetMapping
    public Result<List<AreaDTO>> getAllAreas() {
        return Result.success(areaService.getAllAreas());
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