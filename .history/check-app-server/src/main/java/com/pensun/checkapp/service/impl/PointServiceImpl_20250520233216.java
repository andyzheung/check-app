package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.PointDTO;
import com.pensun.checkapp.entity.InspectionPoint;
import com.pensun.checkapp.mapper.PointMapper;
import com.pensun.checkapp.service.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private PointMapper pointMapper;

    @Override
    public Result list(String keyword, Integer page, Integer size) {
        LambdaQueryWrapper<InspectionPoint> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(InspectionPoint::getName, keyword)
                    .or()
                    .like(InspectionPoint::getDescription, keyword)
                    .or()
                    .like(InspectionPoint::getAddress, keyword);
        }
        wrapper.orderByDesc(InspectionPoint::getCreateTime);

        Page<InspectionPoint> pageResult = pointMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result getById(Long id) {
        InspectionPoint point = pointMapper.selectById(id);
        if (point == null) {
            return Result.error("巡检点不存在");
        }
        return Result.success(point);
    }

    @Override
    @Transactional
    public Result create(PointDTO pointDTO) {
        InspectionPoint point = new InspectionPoint();
        BeanUtils.copyProperties(pointDTO, point);
        point.setStatus("ACTIVE");
        pointMapper.insert(point);
        return Result.success();
    }

    @Override
    @Transactional
    public Result update(Long id, PointDTO pointDTO) {
        InspectionPoint point = pointMapper.selectById(id);
        if (point == null) {
            return Result.error("巡检点不存在");
        }
        BeanUtils.copyProperties(pointDTO, point);
        pointMapper.updateById(point);
        return Result.success();
    }

    @Override
    @Transactional
    public Result delete(Long id) {
        pointMapper.deleteById(id);
        return Result.success();
    }
} 