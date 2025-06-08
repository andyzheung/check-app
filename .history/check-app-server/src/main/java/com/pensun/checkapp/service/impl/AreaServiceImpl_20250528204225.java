package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.ResultCode;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.AreaType;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.AreaTypeMapper;
import com.pensun.checkapp.service.AreaService;
import com.pensun.checkapp.utils.QRCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;
    private final AreaTypeMapper areaTypeMapper;
    private final QRCodeUtils qrCodeUtils;
    
    private static final Pattern AREA_CODE_PATTERN = Pattern.compile("^AREA[A-C]\\d{3}$");

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
            BeanUtils.copyProperties(area, areaDTO);
            areaDTO.setAreaCode(area.getCode()); // 设置DTO的areaCode
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
        // 验证区域编码格式
        if (!AREA_CODE_PATTERN.matcher(areaCode).matches()) {
            throw new RuntimeException("区域编码格式不正确，应为AREA[A-C]加3位数字，例如：AREAA001");
        }
        
        // 查询区域信息
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getCode, areaCode); // 使用正确的字段名
        queryWrapper.eq(Area::getDeleted, 0);
        Area area = areaMapper.selectOne(queryWrapper);
        
        if (area == null) {
            throw new RuntimeException("区域不存在");
        }
        
        // 转换为DTO
        AreaDTO areaDTO = new AreaDTO();
        BeanUtils.copyProperties(area, areaDTO);
        areaDTO.setAreaCode(area.getCode()); // 设置DTO的areaCode
        
        // 查询区域类型名称
        if (area.getAreaType() != null) {
            LambdaQueryWrapper<AreaType> typeQuery = new LambdaQueryWrapper<>();
            typeQuery.eq(AreaType::getTypeCode, area.getAreaType());
            AreaType areaType = areaTypeMapper.selectOne(typeQuery);
            if (areaType != null) {
                areaDTO.setAreaTypeName(areaType.getTypeName());
            }
        }
        
        return areaDTO;
    }

    @Override
    public String generateQRCode(Long id) {
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }
        
        // 生成二维码数据
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("areaCode", area.getCode()); // 使用正确的字段名
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
} 