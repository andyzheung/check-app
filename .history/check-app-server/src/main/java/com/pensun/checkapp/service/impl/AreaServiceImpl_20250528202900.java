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

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;
    private final AreaTypeMapper areaTypeMapper;
    private final QRCodeUtils qrCodeUtils;

    @Override
    public AreaDTO getAreaByCode(String areaCode) {
        // 查询区域信息
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getAreaCode, areaCode);
        queryWrapper.eq(Area::getDeleted, 0);
        Area area = areaMapper.selectOne(queryWrapper);
        
        if (area == null) {
            throw new RuntimeException(ResultCode.AREA_NOT_EXIST.getMessage());
        }
        
        // 转换为DTO
        AreaDTO areaDTO = new AreaDTO();
        BeanUtils.copyProperties(area, areaDTO);
        
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
            throw new RuntimeException(ResultCode.AREA_NOT_EXIST.getMessage());
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
} 