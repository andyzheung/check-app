package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.dto.RecordDTO;
import com.pensun.checkapp.entity.Area;
import com.pensun.checkapp.entity.InspectionItem;
import com.pensun.checkapp.entity.InspectionRecord;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.mapper.AreaMapper;
import com.pensun.checkapp.mapper.InspectionItemMapper;
import com.pensun.checkapp.mapper.InspectionRecordMapper;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 巡检记录Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<InspectionRecordMapper, InspectionRecord> implements RecordService {

    private final InspectionRecordMapper recordMapper;
    private final InspectionItemMapper itemMapper;
    private final AreaMapper areaMapper;
    private final UserMapper userMapper;

    @Override
    public PageResult<RecordDTO> getRecordList(int page, int pageSize, Long areaId, Long inspectorId, String status, LocalDate startDate, LocalDate endDate) {
        // 分页查询
        Page<InspectionRecord> recordPage = new Page<>(page, pageSize);
        IPage<InspectionRecord> resultPage = recordMapper.selectRecordPage(recordPage, areaId, inspectorId, status, startDate, endDate);

        // 转换为DTO
        List<RecordDTO> recordDTOList = resultPage.getRecords().stream().map(record -> {
            RecordDTO recordDTO = new RecordDTO();
            BeanUtils.copyProperties(record, recordDTO);
            recordDTO.setInspectionTime(record.getStartTime());
            
            // 查询区域名称
            Area area = areaMapper.selectById(record.getAreaId());
            if (area != null) {
                recordDTO.setAreaName(area.getAreaName());
            }
            
            // 查询巡检人员名称
            User inspector = userMapper.selectById(record.getInspectorId());
            if (inspector != null) {
                recordDTO.setInspectorName(inspector.getRealName());
            }

            // 查询所有巡检项，判断整体状态
            int abnormalCount = itemMapper.countAbnormalItems(record.getId());
            boolean hasAbnormalItem = abnormalCount > 0;
            recordDTO.setStatus(hasAbnormalItem ? "abnormal" : "normal");
            
            // 如果数据库中的状态与实际状态不一致，更新数据库
            if (hasAbnormalItem && !"abnormal".equals(record.getStatus())) {
                record.setStatus("abnormal");
                recordMapper.updateById(record);
            }
            
            return recordDTO;
        }).collect(Collectors.toList());

        // 返回分页结果
        return PageResult.of(recordDTOList, resultPage.getTotal());
    }

    @Override
    public RecordDTO getRecordDetail(String id) {
        // 查询记录
        InspectionRecord record = null;
        try {
            Long recordId = Long.parseLong(id);
            record = recordMapper.selectById(recordId);
        } catch (NumberFormatException e) {
            // 如果id不是数字，则按照记录编号查询
            LambdaQueryWrapper<InspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InspectionRecord::getRecordNo, id);
            queryWrapper.eq(InspectionRecord::getDeleted, 0);
            record = recordMapper.selectOne(queryWrapper);
        }

        if (record == null || record.getDeleted() == 1) {
            throw new RuntimeException("巡检记录不存在");
        }

        // 转换为DTO
        RecordDTO recordDTO = new RecordDTO();
        BeanUtils.copyProperties(record, recordDTO);
        recordDTO.setInspectionTime(record.getStartTime());

        // 查询区域名称
        Area area = areaMapper.selectById(record.getAreaId());
        if (area != null) {
            recordDTO.setAreaName(area.getAreaName());
        }

        // 查询巡检人员名称
        User inspector = userMapper.selectById(record.getInspectorId());
        if (inspector != null) {
            recordDTO.setInspectorName(inspector.getRealName());
        }

        // 查询环境巡检项
        List<InspectionItem> environmentItems = itemMapper.selectByRecordIdAndType(record.getId(), "environment");
        recordDTO.setEnvironmentItems(environmentItems.stream().map(item -> {
            RecordDTO.InspectionItemDTO itemDTO = new RecordDTO.InspectionItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setName(item.getName());
            itemDTO.setStatus(item.getStatus());
            itemDTO.setRemark(item.getRemark());
            return itemDTO;
        }).collect(Collectors.toList()));

        // 查询安全巡检项
        List<InspectionItem> securityItems = itemMapper.selectByRecordIdAndType(record.getId(), "security");
        recordDTO.setSecurityItems(securityItems.stream().map(item -> {
            RecordDTO.InspectionItemDTO itemDTO = new RecordDTO.InspectionItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setName(item.getName());
            itemDTO.setStatus(item.getStatus());
            itemDTO.setRemark(item.getRemark());
            return itemDTO;
        }).collect(Collectors.toList()));

        return recordDTO;
    }

    @Override
    public InspectionRecordDetailDTO getRecordDetailV2(Long id) {
        // 查询巡检记录
        InspectionRecord record = getById(id);
        if (record == null || record.getDeleted() == 1) {
            throw new BusinessException("巡检记录不存在");
        }

        // 转换为DTO
        InspectionRecordDetailDTO dto = new InspectionRecordDetailDTO();
        BeanUtils.copyProperties(record, dto);

        // 查询区域信息
        Area area = areaMapper.selectById(record.getAreaId());
        if (area != null) {
            InspectionRecordDetailDTO.AreaDTO areaDTO = new InspectionRecordDetailDTO.AreaDTO();
            BeanUtils.copyProperties(area, areaDTO);
            dto.setAreaInfo(areaDTO);
        }

        // 查询巡检人员信息
        User inspector = userMapper.selectById(record.getInspectorId());
        if (inspector != null) {
            InspectionRecordDetailDTO.UserDTO userDTO = new InspectionRecordDetailDTO.UserDTO();
            BeanUtils.copyProperties(inspector, userDTO);
            dto.setInspectorInfo(userDTO);
        }

        // 查询环境巡检项
        List<InspectionItem> environmentItems = itemMapper.selectByRecordIdAndType(id, "environment");
        dto.setEnvironmentItems(environmentItems.stream().map(item -> {
            InspectionRecordDetailDTO.InspectionItemDTO itemDTO = new InspectionRecordDetailDTO.InspectionItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList()));

        // 查询安全巡检项
        List<InspectionItem> securityItems = itemMapper.selectByRecordIdAndType(id, "security");
        dto.setSecurityItems(securityItems.stream().map(item -> {
            InspectionRecordDetailDTO.InspectionItemDTO itemDTO = new InspectionRecordDetailDTO.InspectionItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList()));

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRecord(RecordDTO recordDTO) {
        return create(recordDTO).getCode() == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result create(RecordDTO recordDTO) {
        // 参数校验
        if (recordDTO.getAreaId() == null) {
            return Result.failed("区域ID不能为空");
        }
        if (recordDTO.getEnvironmentItems() == null || recordDTO.getEnvironmentItems().isEmpty()) {
            return Result.failed("环境巡检项不能为空");
        }
        if (recordDTO.getSecurityItems() == null || recordDTO.getSecurityItems().isEmpty()) {
            return Result.failed("安全巡检项不能为空");
        }

        try {
            // 创建巡检记录
            InspectionRecord record = new InspectionRecord();
            BeanUtils.copyProperties(recordDTO, record);
            
            // 生成记录编号
            String recordNo = "R" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            record.setRecordNo(recordNo);
            
            // 设置时间
            if (record.getStartTime() == null) {
                record.setStartTime(LocalDateTime.now());
            }
            if (record.getEndTime() == null) {
                record.setEndTime(LocalDateTime.now());
            }
            
            // 设置巡检人员ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                record.setInspectorId(user.getId());
            } else {
                return Result.failed("未获取到当前登录用户信息");
            }
            
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            record.setDeleted(0);
            
            // 保存记录
            log.info("保存巡检记录: recordNo={}, areaId={}, inspectorId={}", 
                record.getRecordNo(), record.getAreaId(), record.getInspectorId());
            boolean success = recordMapper.insert(record) > 0;
            if (!success) {
                return Result.failed("保存巡检记录失败");
            }
            
            // 批量保存巡检项
            List<InspectionItem> items = new ArrayList<>();
            
            // 添加环境巡检项
            if (recordDTO.getEnvironmentItems() != null) {
                for (RecordDTO.InspectionItemDTO itemDTO : recordDTO.getEnvironmentItems()) {
                    items.add(createInspectionItem(record.getId(), itemDTO, "environment"));
                }
            }
            
            // 添加安全巡检项
            if (recordDTO.getSecurityItems() != null) {
                for (RecordDTO.InspectionItemDTO itemDTO : recordDTO.getSecurityItems()) {
                    items.add(createInspectionItem(record.getId(), itemDTO, "security"));
                }
            }
            
            // 批量插入巡检项
            log.info("保存巡检项: recordId={}, itemCount={}", record.getId(), items.size());
            for (InspectionItem item : items) {
                itemMapper.insert(item);
            }
            
            // 检查是否有异常项
            int abnormalCount = itemMapper.countAbnormalItems(record.getId());
            if (abnormalCount > 0) {
                record.setStatus("abnormal");
                recordMapper.updateById(record);
            }
            
            return Result.success();
        } catch (Exception e) {
            log.error("创建巡检记录失败", e);
            throw new RuntimeException("创建巡检记录失败: " + e.getMessage());
        }
    }

    private InspectionItem createInspectionItem(Long recordId, RecordDTO.InspectionItemDTO itemDTO, String type) {
        InspectionItem item = new InspectionItem();
        item.setRecordId(recordId);
        item.setName(itemDTO.getName());
        item.setType(type);
        item.setStatus(itemDTO.getStatus());
        item.setRemark(itemDTO.getRemark());
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setDeleted(0);
        return item;
    }

    @Override
    public Map<String, Object> getRecordStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计总巡检次数
        int totalInspections = recordMapper.countTotalInspections();
        stats.put("totalInspections", totalInspections);
        
        // 统计总巡检人员数量
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getRole, "user");
        userQueryWrapper.eq(User::getDeleted, 0);
        int totalInspectors = userMapper.selectCount(userQueryWrapper).intValue();
        stats.put("totalInspectors", totalInspectors);
        
        // 获取本周时间范围
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekTime = endOfWeek.atTime(23, 59, 59);
        
        // 统计本周巡检次数
        int weeklyInspections = recordMapper.countWeeklyInspections(startOfWeekTime, endOfWeekTime);
        stats.put("weeklyInspections", weeklyInspections);
        
        // 其他统计数据
        stats.put("activeInspectors", 48);
        stats.put("weeklyInspectors", 24);
        stats.put("weeklyActiveInspectors", 16);
        stats.put("weeklyInspectionsRate", 12.5);
        stats.put("weeklyInspectorsRate", 4.2);
        
        return stats;
    }

    @Override
    public Map<String, Object> getWeeklyTrend() {
        Map<String, Object> trend = new HashMap<>();
        
        // 生成周数据
        List<String> dates = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            dates.add("第" + i + "周");
        }
        trend.put("dates", dates);
        
        // 生成巡检数据
        List<Integer> inspections = new ArrayList<>();
        inspections.add(120);
        inspections.add(132);
        inspections.add(101);
        inspections.add(134);
        inspections.add(90);
        inspections.add(230);
        inspections.add(210);
        trend.put("inspections", inspections);
        
        // 生成问题数据
        List<Integer> issues = new ArrayList<>();
        issues.add(20);
        issues.add(18);
        issues.add(15);
        issues.add(22);
        issues.add(10);
        issues.add(25);
        issues.add(18);
        trend.put("issues", issues);
        
        return trend;
    }

    @Override
    public Object getInspectorRanking() {
        // 模拟巡检人员排名数据
        List<Map<String, Object>> ranking = new ArrayList<>();
        
        Map<String, Object> inspector1 = new HashMap<>();
        inspector1.put("name", "张三");
        inspector1.put("count", 120);
        ranking.add(inspector1);
        
        Map<String, Object> inspector2 = new HashMap<>();
        inspector2.put("name", "李四");
        inspector2.put("count", 110);
        ranking.add(inspector2);
        
        Map<String, Object> inspector3 = new HashMap<>();
        inspector3.put("name", "王五");
        inspector3.put("count", 98);
        ranking.add(inspector3);
        
        Map<String, Object> inspector4 = new HashMap<>();
        inspector4.put("name", "赵六");
        inspector4.put("count", 80);
        ranking.add(inspector4);
        
        Map<String, Object> inspector5 = new HashMap<>();
        inspector5.put("name", "钱七");
        inspector5.put("count", 75);
        ranking.add(inspector5);
        
        return ranking;
    }

    @Override
    public byte[] exportRecords(Long areaId, Long inspectorId, String status, LocalDate startDate, LocalDate endDate) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("巡检记录");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("记录编号");
            headerRow.createCell(1).setCellValue("区域名称");
            headerRow.createCell(2).setCellValue("巡检人员");
            headerRow.createCell(3).setCellValue("巡检时间");
            headerRow.createCell(4).setCellValue("巡检状态");
            headerRow.createCell(5).setCellValue("备注");
            
            // 查询数据
            LambdaQueryWrapper<InspectionRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InspectionRecord::getDeleted, 0);
            if (areaId != null) {
                queryWrapper.eq(InspectionRecord::getAreaId, areaId);
            }
            if (inspectorId != null) {
                queryWrapper.eq(InspectionRecord::getInspectorId, inspectorId);
            }
            if (status != null && !status.isEmpty()) {
                queryWrapper.eq(InspectionRecord::getStatus, status);
            }
            if (startDate != null) {
                queryWrapper.ge(InspectionRecord::getStartTime, startDate.atStartOfDay());
            }
            if (endDate != null) {
                queryWrapper.le(InspectionRecord::getStartTime, endDate.atTime(23, 59, 59));
            }
            queryWrapper.orderByDesc(InspectionRecord::getStartTime);
            
            List<InspectionRecord> records = recordMapper.selectList(queryWrapper);
            
            // 填充数据
            int rowNum = 1;
            for (InspectionRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getRecordNo());
                
                // 查询区域名称
                Area area = areaMapper.selectById(record.getAreaId());
                row.createCell(1).setCellValue(area != null ? area.getAreaName() : "");
                
                // 查询巡检人员名称
                User inspector = userMapper.selectById(record.getInspectorId());
                row.createCell(2).setCellValue(inspector != null ? inspector.getRealName() : "");
                
                // 格式化巡检时间
                row.createCell(3).setCellValue(record.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                
                // 格式化状态
                row.createCell(4).setCellValue("normal".equals(record.getStatus()) ? "正常" : "异常");
                
                row.createCell(5).setCellValue(record.getRemark() != null ? record.getRemark() : "");
            }
            
            // 调整列宽
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 输出为字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("导出巡检记录失败", e);
        }
    }

    @Override
    public Result list(String keyword, Integer page, Integer size) {
        // TODO: 实现分页查询
        return null;
    }

    @Override
    public Result getById(Long id) {
        // TODO: 实现获取详情
        return null;
    }

    @Override
    public Result update(Long id, RecordDTO recordDTO) {
        // TODO: 实现更新记录
        return null;
    }

    @Override
    public Result delete(Long id) {
        InspectionRecord record = super.getById(id);
        if (record == null) {
            return Result.failed("记录不存在");
        }
        record.setDeleted(1);
        record.setUpdateTime(java.time.LocalDateTime.now());
        super.updateById(record);
        return Result.success();
    }
} 