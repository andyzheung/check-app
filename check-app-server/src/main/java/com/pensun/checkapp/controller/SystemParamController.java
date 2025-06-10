package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.SystemParamDTO;
import com.pensun.checkapp.service.SystemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统参数Controller
 * 
 * [ADMIN] - 该接口主要供管理后台使用
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/system/params")
public class SystemParamController {

    @Autowired
    private SystemParamService systemParamService;

    /**
     * 获取所有系统参数
     * [ADMIN]
     *
     * @return 系统参数Map
     */
    @GetMapping
    public Result<Map<String, String>> getAllParams() {
        log.info("获取所有系统参数");
        Map<String, String> params = systemParamService.getAllParams();
        return Result.success(params);
    }

    /**
     * 获取指定系统参数
     * [COMMON]
     *
     * @param key 参数键
     * @return 参数值
     */
    @GetMapping("/{key}")
    public Result<String> getParam(@PathVariable String key) {
        log.info("获取系统参数: key={}", key);
        String value = systemParamService.getParamValue(key);
        return Result.success(value);
    }

    /**
     * 设置系统参数
     * [ADMIN]
     *
     * @param paramDTO 参数DTO
     * @return 设置结果
     */
    @PostMapping
    public Result<Boolean> setParam(@RequestBody SystemParamDTO paramDTO) {
        log.info("设置系统参数: key={}, value={}", paramDTO.getParamKey(), paramDTO.getParamValue());
        boolean result = systemParamService.setParamValue(
                paramDTO.getParamKey(), 
                paramDTO.getParamValue(), 
                paramDTO.getParamDesc());
        return Result.success(result);
    }

    /**
     * 删除系统参数
     * [ADMIN]
     *
     * @param key 参数键
     * @return 删除结果
     */
    @DeleteMapping("/{key}")
    public Result<Boolean> deleteParam(@PathVariable String key) {
        log.info("删除系统参数: key={}", key);
        boolean result = systemParamService.removeByKey(key);
        return Result.success(result);
    }
} 