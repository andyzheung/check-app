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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;
    private final AreaTypeMapper areaTypeMapper;

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
        
        // TODO: 实现二维码生成逻辑
        return null;
    }

    @Override
    public Boolean verifyQRCode(String qrData) {
        // 验证二维码格式：AREA[A-C]\d{3}
        if (!qrData.matches("^AREA[A-C]\\d{3}$")) {
            return false;
        }
        
        // 查询区域是否存在
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getAreaCode, qrData);
        queryWrapper.eq(Area::getDeleted, 0);
        return areaMapper.selectCount(queryWrapper) > 0;
    }
} 