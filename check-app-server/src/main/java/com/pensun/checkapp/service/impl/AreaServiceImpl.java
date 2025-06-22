package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.dto.AreaConfigDTO;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.AreaType;
import com.pensun.checkapp.entity.InspectionItemTemplate;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.AreaTypeMapper;
import com.pensun.checkapp.service.AreaService;
import com.pensun.checkapp.service.InspectionItemTemplateService;
import com.pensun.checkapp.utils.QRCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;
    private final AreaTypeMapper areaTypeMapper;
    private final QRCodeUtils qrCodeUtils;
    private final InspectionItemTemplateService inspectionItemTemplateService;
    
    // private static final Pattern AREA_CODE_PATTERN = Pattern.compile("^AREA[A-C]\\\\d{3}$");

    @Override
    public List<AreaDTO> getAllAreas() {
        // 查询所有未删除的区域
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getDeleted, 0);
        List<Area> areas = areaMapper.selectList(queryWrapper);
        
        // 查询所有区域类型
        LambdaQueryWrapper<AreaType> typeQuery = new LambdaQueryWrapper<>();
        List<AreaType> areaTypes = areaTypeMapper.selectList(typeQuery);
        Map<String, String> typeNameMap = new HashMap<>();
        for (AreaType type : areaTypes) {
            typeNameMap.put(type.getTypeCode(), type.getTypeName());
        }
        
        // 转换为DTO
        List<AreaDTO> areaDTOs = new ArrayList<>();
        for (Area area : areas) {
            AreaDTO areaDTO = new AreaDTO();
            
            // 手动映射字段，避免字段名不匹配问题
            areaDTO.setId(area.getId());
            areaDTO.setAreaCode(area.getAreaCode()); // Area.areaCode -> AreaDTO.areaCode
            areaDTO.setAreaName(area.getName()); // Area.name -> AreaDTO.areaName
            areaDTO.setAreaType(area.getAreaType());
            areaDTO.setStatus(area.getStatus());
            areaDTO.setDescription(area.getDescription());
            areaDTO.setAddress(area.getAddress());
            areaDTO.setModuleCount(area.getModuleCount());
            areaDTO.setConfigJson(area.getConfigJson());
            areaDTO.setQrCodeUrl(area.getQrCodeUrl());
            areaDTO.setCreateTime(area.getCreateTime());
            areaDTO.setUpdateTime(area.getUpdateTime());
            
            // 设置区域类型名称
            if (area.getAreaType() != null) {
                areaDTO.setAreaTypeName(typeNameMap.get(area.getAreaType()));
            }
            
            areaDTOs.add(areaDTO);
        }
        
        return areaDTOs;
    }

    @Override
    public AreaDTO getAreaByCode(String areaCode) {
        try {
            log.debug("开始查询区域信息，区域编码：{}", areaCode);
            
            // 1. 查询区域信息
            LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Area::getAreaCode, areaCode.toUpperCase())
                    .eq(Area::getDeleted, 0);
            
            Area area = areaMapper.selectOne(queryWrapper);
            
            if (area == null) {
                log.warn("区域不存在，区域编码：{}", areaCode);
                throw new RuntimeException("区域不存在");
            }
            
            log.debug("查询到区域信息：{}", area);
            
            // 2. 转换为DTO
            AreaDTO areaDTO = new AreaDTO();
            
            // 手动映射字段，避免字段名不匹配问题
            areaDTO.setId(area.getId());
            areaDTO.setAreaCode(area.getAreaCode()); // Area.areaCode -> AreaDTO.areaCode
            areaDTO.setAreaName(area.getName()); // Area.name -> AreaDTO.areaName
            areaDTO.setAreaType(area.getAreaType());
            areaDTO.setStatus(area.getStatus());
            areaDTO.setDescription(area.getDescription());
            areaDTO.setAddress(area.getAddress());
            areaDTO.setModuleCount(area.getModuleCount());
            areaDTO.setConfigJson(area.getConfigJson());
            areaDTO.setQrCodeUrl(area.getQrCodeUrl());
            areaDTO.setCreateTime(area.getCreateTime());
            areaDTO.setUpdateTime(area.getUpdateTime());
            
            // 3. 查询并设置区域类型名称
            if (area.getAreaType() != null) {
                AreaType areaType = areaTypeMapper.selectOne(new LambdaQueryWrapper<AreaType>().eq(AreaType::getTypeCode, area.getAreaType()));
                if (areaType != null) {
                    areaDTO.setAreaTypeName(areaType.getTypeName());
                }
            }

            // 4. 查询并设置巡检项目模板 (核心改造点)
            List<InspectionItemTemplate> inspectionItems = inspectionItemTemplateService.getTemplatesByAreaType(area.getAreaType());
            areaDTO.setInspectionItems(inspectionItems);
            
            // 5. 处理数据中心模块配置 (关键修复)
            if ("D".equals(area.getAreaType()) && area.getConfigJson() != null) {
                try {
                    // 解析JSON配置，提取模块信息
                    com.alibaba.fastjson.JSONObject config = com.alibaba.fastjson.JSON.parseObject(area.getConfigJson());
                    if (config.containsKey("modules")) {
                        com.alibaba.fastjson.JSONArray modulesArray = config.getJSONArray("modules");
                        List<AreaDTO.ModuleDTO> modules = new ArrayList<>();
                        for (int i = 0; i < modulesArray.size(); i++) {
                            com.alibaba.fastjson.JSONObject moduleObj = modulesArray.getJSONObject(i);
                            AreaDTO.ModuleDTO moduleDTO = new AreaDTO.ModuleDTO();
                            moduleDTO.setId(moduleObj.getLong("id"));
                            moduleDTO.setName(moduleObj.getString("name"));
                            moduleDTO.setType(moduleObj.getString("type"));
                            modules.add(moduleDTO);
                        }
                        areaDTO.setModules(modules);
                    }
                } catch (Exception e) {
                    log.error("解析区域模块配置失败: {}", e.getMessage());
                }
            }
            
            return areaDTO;
        } catch (Exception e) {
            log.error("查询区域信息失败", e);
            throw new RuntimeException("查询区域信息失败: " + e.getMessage());
        }
    }

    @Override
    public String generateQRCode(Long id) {
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        // 生成二维码数据
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("areaCode", area.getAreaCode());
        qrData.put("timestamp", System.currentTimeMillis());
        
        // 生成签名
        String signature = qrCodeUtils.generateSignature(qrData);
        qrData.put("signature", signature);
        
        // 生成二维码
        String qrCodeUrl = qrCodeUtils.generateQRCode(qrData);
        
        // 更新区域的二维码URL
        area.setQrCodeUrl(qrCodeUrl);
        areaMapper.updateById(area);
        
        return qrCodeUrl;
    }

    @Override
    public Boolean verifyQRCode(String qrData) {
        return qrCodeUtils.verifyQRCode(qrData);
    }

    @Override
    public String getQRCode(Long id) {
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        // 如果区域已有二维码URL，直接返回
        if (StringUtils.hasText(area.getQrCodeUrl())) {
            return area.getQrCodeUrl();
        }
        
        // 否则生成新的二维码
        return generateQRCode(id);
    }

    @Override
    public Result<Page<AreaDTO>> getAllAreas(String status, String type, String keyword, Integer page, Integer size) {
        log.info("获取区域列表（带过滤） - status: {}, type: {}, keyword: {}, page: {}, size: {}", 
                status, type, keyword, page, size);
        
        // 查询所有区域
        List<AreaDTO> allAreas = getAllAreas();
        log.info("原始区域列表大小: {}", allAreas.size());
        
        // 应用过滤条件
        List<AreaDTO> filteredAreas = allAreas.stream()
            .filter(area -> StringUtils.isEmpty(status) || status.equalsIgnoreCase(area.getStatus()))
            .filter(area -> StringUtils.isEmpty(type) || type.equalsIgnoreCase(area.getAreaType()))
            .filter(area -> StringUtils.isEmpty(keyword) || 
                    (area.getAreaName() != null && area.getAreaName().contains(keyword)) || 
                    (area.getAreaCode() != null && area.getAreaCode().contains(keyword)))
            .collect(Collectors.toList());
        
        log.info("过滤后区域列表大小: {}", filteredAreas.size());
        
        // 手动分页
        int total = filteredAreas.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        // 防止越界
        if (start >= total) {
            start = 0;
            end = 0;
        }
        
        List<AreaDTO> pagedAreas = filteredAreas.subList(start, end);
        
        // 构建分页对象
        Page<AreaDTO> pageResult = new Page<>(page, size, total);
        pageResult.setRecords(pagedAreas);
        
        log.info("返回区域列表（分页） - 总数: {}, 当前页数量: {}", total, pagedAreas.size());
        return Result.success(pageResult);
    }
    
    @Override
    public void updateAreaConfig(Long id, AreaConfigDTO configDTO) {
        log.info("更新区域配置 - ID: {}, 配置: {}", id, configDTO);
        
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        // 更新模块数量和配置JSON
        area.setModuleCount(configDTO.getModuleCount());
        area.setConfigJson(configDTO.getConfigJson());
        
        int result = areaMapper.updateById(area);
        if (result <= 0) {
            throw new RuntimeException("更新区域配置失败");
        }
        
        log.info("区域配置更新成功 - ID: {}", id);
    }
    
    @Override
    public Long createArea(AreaDTO areaDTO) {
        log.info("创建新区域: {}", areaDTO);
        
        Area area = new Area();
        
        // 手动映射字段，避免字段名不匹配问题
        area.setAreaCode(areaDTO.getAreaCode()); // AreaDTO.areaCode -> Area.areaCode
        area.setName(areaDTO.getAreaName()); // AreaDTO.areaName -> Area.name
        area.setAreaType(areaDTO.getAreaType());
        area.setDescription(areaDTO.getDescription());
        area.setAddress(areaDTO.getAddress());
        area.setModuleCount(areaDTO.getModuleCount());
        area.setConfigJson(areaDTO.getConfigJson());
        
        // 设置默认值
        if (areaDTO.getStatus() == null) {
            area.setStatus("active");
        } else {
            area.setStatus(areaDTO.getStatus());
        }
        
        int result = areaMapper.insert(area);
        if (result <= 0) {
            throw new RuntimeException("创建区域失败");
        }
        
        log.info("区域创建成功: id={}, code={}", area.getId(), area.getAreaCode());
        return area.getId();
    }

    @Override
    public void updateArea(Long id, AreaDTO areaDTO) {
        log.info("更新区域信息: id={}, area={}", id, areaDTO);
        
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        // 手动映射字段，避免字段名不匹配问题
        area.setAreaCode(areaDTO.getAreaCode()); // AreaDTO.areaCode -> Area.areaCode
        area.setName(areaDTO.getAreaName()); // AreaDTO.areaName -> Area.name
        area.setAreaType(areaDTO.getAreaType());
        area.setDescription(areaDTO.getDescription());
        area.setAddress(areaDTO.getAddress());
        area.setModuleCount(areaDTO.getModuleCount());
        area.setConfigJson(areaDTO.getConfigJson());
        if (areaDTO.getStatus() != null) {
            area.setStatus(areaDTO.getStatus());
        }
        
        area.setId(id); // 确保ID不被覆盖
        
        int result = areaMapper.updateById(area);
        if (result <= 0) {
            throw new RuntimeException("更新区域失败");
        }
        
        log.info("区域更新成功: id={}", id);
    }

    @Override
    public void deleteArea(Long id) {
        log.info("删除区域: id={}", id);
        
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        int result = areaMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除区域失败");
        }
        
        log.info("区域删除成功: id={}", id);
    }
} 