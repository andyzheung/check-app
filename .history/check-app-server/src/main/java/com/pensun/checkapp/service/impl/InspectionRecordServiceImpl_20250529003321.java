package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.dto.InspectionRecordDetailDTO;
import com.pensun.checkapp.entity.*;
import com.pensun.checkapp.mapper.*;
import com.pensun.checkapp.service.InspectionRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionRecordServiceImpl extends ServiceImpl<InspectionRecordMapper, InspectionRecord> implements InspectionRecordService {
    private final InspectionRecordMapper inspectionRecordMapper;
    private final InspectionItemMapper inspectionItemMapper;
    private final AreaMapper areaMapper;
    private final AreaTypeMapper areaTypeMapper;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "recordDetail", key = "#id", unless = "#result == null")
    public InspectionRecordDetailDTO getRecordDetail(Long id) {
        log.debug("获取巡检记录详情，记录ID：{}", id);

        // 获取巡检记录
        InspectionRecord record = inspectionRecordMapper.selectById(id);
        if (record == null || record.getDeleted() == 1) {
            log.warn("巡检记录不存在，记录ID：{}", id);
            throw new RuntimeException("巡检记录不存在");
        }

        // 创建返回对象
        InspectionRecordDetailDTO detailDTO = new InspectionRecordDetailDTO();
        BeanUtils.copyProperties(record, detailDTO);

        try {
            // 获取区域信息
            Area area = areaMapper.selectById(record.getAreaId());
            if (area != null) {
                AreaDTO areaDTO = new AreaDTO();
                BeanUtils.copyProperties(area, areaDTO);
                
                // 获取区域类型名称
                if (area.getAreaType() != null) {
                    AreaType areaType = areaTypeMapper.selectById(area.getAreaType());
                    if (areaType != null) {
                        areaDTO.setAreaTypeName(areaType.getTypeName());
                    }
                }
                
                detailDTO.setAreaInfo(areaDTO);
            }

            // 获取巡检人员信息
            User inspector = userMapper.selectById(record.getInspectorId());
            if (inspector != null) {
                InspectionRecordDetailDTO.UserDTO userDTO = new InspectionRecordDetailDTO.UserDTO();
                userDTO.setId(inspector.getId());
                userDTO.setUsername(inspector.getUsername());
                userDTO.setRealName(inspector.getRealName());
                detailDTO.setInspectorInfo(userDTO);
            }

            // 获取巡检项列表
            LambdaQueryWrapper<InspectionItem> itemQuery = new LambdaQueryWrapper<>();
            itemQuery.eq(InspectionItem::getRecordId, id)
                    .eq(InspectionItem::getDeleted, 0);
            List<InspectionItem> items = inspectionItemMapper.selectList(itemQuery);

            // 分类巡检项
            List<InspectionRecordDetailDTO.InspectionItemDTO> environmentItems = new ArrayList<>();
            List<InspectionRecordDetailDTO.InspectionItemDTO> securityItems = new ArrayList<>();
            boolean hasAbnormalItem = false;

            for (InspectionItem item : items) {
                InspectionRecordDetailDTO.InspectionItemDTO itemDTO = new InspectionRecordDetailDTO.InspectionItemDTO();
                BeanUtils.copyProperties(item, itemDTO);

                if ("abnormal".equals(item.getStatus())) {
                    hasAbnormalItem = true;
                }

                if ("environment".equals(item.getType())) {
                    environmentItems.add(itemDTO);
                } else if ("security".equals(item.getType())) {
                    securityItems.add(itemDTO);
                }
            }

            // 如果有任何一个巡检项异常，整个巡检记录就是异常状态
            if (hasAbnormalItem) {
                detailDTO.setStatus("abnormal");
            }

            detailDTO.setEnvironmentItems(environmentItems);
            detailDTO.setSecurityItems(securityItems);

            log.debug("获取巡检记录详情成功，记录ID：{}", id);
            return detailDTO;
        } catch (Exception e) {
            log.error("获取巡检记录详情失败", e);
            throw new RuntimeException("获取巡检记录详情失败: " + e.getMessage());
        }
    }
} 