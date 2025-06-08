package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    private final AreaMapper areaMapper;

    @Override
    public Area getByCode(String code) {
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getCode, code);
        queryWrapper.eq(Area::getDeleted, 0);
        return areaMapper.selectOne(queryWrapper);
    }
} 