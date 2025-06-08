package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域Service实现类
 */
@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    private final AreaMapper areaMapper;

    @Override
    public PageResult<AreaDTO> getAreaList(int page, int pageSize, String name, String status) {
        // 构建查询条件
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getDeleted, 0);
        if (StringUtils.hasText(name)) {
            queryWrapper.like(Area::getName, name);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(Area::getStatus, status);
        }
        queryWrapper.orderByAsc(Area::getId);

        // 分页查询
        Page<Area> areaPage = new Page<>(page, pageSize);
        Page<Area> resultPage = areaMapper.selectPage(areaPage, queryWrapper);

        // 转换为DTO
        List<AreaDTO> areaDTOList = resultPage.getRecords().stream().map(area -> {
            AreaDTO areaDTO = new AreaDTO();
            BeanUtils.copyProperties(area, areaDTO);
            // 获取巡检项
            List<String> checkItems = areaMapper.selectCheckItemNamesByAreaId(area.getId());
            areaDTO.setCheckItems(checkItems);
            return areaDTO;
        }).collect(Collectors.toList());

        // 返回分页结果
        return PageResult.of(areaDTOList, resultPage.getTotal());
    }

    @Override
    public AreaDTO getAreaDetail(Long id) {
        // 查询区域
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }

        // 转换为DTO
        AreaDTO areaDTO = new AreaDTO();
        BeanUtils.copyProperties(area, areaDTO);
        
        // 获取巡检项
        List<String> checkItems = areaMapper.selectCheckItemNamesByAreaId(id);
        areaDTO.setCheckItems(checkItems);

        return areaDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addArea(AreaDTO areaDTO) {
        // 检查编码是否存在
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getCode, areaDTO.getCode());
        queryWrapper.eq(Area::getDeleted, 0);
        if (areaMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("区域编码已存在");
        }

        // 创建区域
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setCreateTime(LocalDateTime.now());
        area.setUpdateTime(LocalDateTime.now());
        area.setDeleted(0);

        return areaMapper.insert(area) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArea(Long id, AreaDTO areaDTO) {
        // 检查区域是否存在
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }

        // 检查编码是否重复
        if (!area.getCode().equals(areaDTO.getCode())) {
            LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Area::getCode, areaDTO.getCode());
            queryWrapper.eq(Area::getDeleted, 0);
            if (areaMapper.selectCount(queryWrapper) > 0) {
                throw new RuntimeException("区域编码已存在");
            }
        }

        // 更新区域
        BeanUtils.copyProperties(areaDTO, area);
        area.setUpdateTime(LocalDateTime.now());

        return areaMapper.updateById(area) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArea(Long id) {
        // 检查区域是否存在
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }

        // 逻辑删除区域
        area.setDeleted(1);
        area.setUpdateTime(LocalDateTime.now());

        return areaMapper.updateById(area) > 0;
    }

    @Override
    public byte[] getAreaQrCode(Long id) {
        // 查询区域
        Area area = areaMapper.selectById(id);
        if (area == null || area.getDeleted() == 1) {
            throw new RuntimeException("区域不存在");
        }

        try {
            // 生成二维码内容
            String content = "AREA:" + area.getCode();

            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);

            // 转换为图片
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }
} 