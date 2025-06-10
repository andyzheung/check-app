package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.FileUploadDTO;
import com.pensun.checkapp.dto.IssueDTO;
import com.pensun.checkapp.dto.IssueProcessDTO;
import com.pensun.checkapp.entity.Issue;
import com.pensun.checkapp.entity.IssueProcess;
import com.pensun.checkapp.entity.User;
import com.pensun.checkapp.mapper.IssueMapper;
import com.pensun.checkapp.mapper.UserMapper;
import com.pensun.checkapp.service.IssueProcessService;
import com.pensun.checkapp.service.IssueService;
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

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 问题管理Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {

    private final IssueMapper issueMapper;
    private final UserMapper userMapper;
    private final IssueProcessService issueProcessService;

    @Override
    public Result getIssueList(Integer page, Integer size, String status, String type, Long recordId, LocalDate startDate, LocalDate endDate) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<Issue> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Issue::getDeleted, 0);
            
            if (status != null && !status.isEmpty()) {
                queryWrapper.eq(Issue::getStatus, status);
            }
            
            if (type != null && !type.isEmpty()) {
                queryWrapper.eq(Issue::getType, type);
            }
            
            if (recordId != null) {
                queryWrapper.eq(Issue::getRecordId, recordId);
            }
            
            if (startDate != null) {
                queryWrapper.ge(Issue::getCreateTime, startDate.atStartOfDay());
            }
            
            if (endDate != null) {
                queryWrapper.le(Issue::getCreateTime, endDate.plusDays(1).atStartOfDay());
            }
            
            // 按创建时间倒序排序
            queryWrapper.orderByDesc(Issue::getCreateTime);
            
            // 分页查询
            IPage<Issue> issueIPage = baseMapper.selectPage(new Page<>(page, size), queryWrapper);
            
            // 转换为DTO
            List<IssueDTO> issueDTOList = new ArrayList<>();
            if (issueIPage.getRecords() != null && !issueIPage.getRecords().isEmpty()) {
                issueDTOList = issueIPage.getRecords().stream().map(issue -> {
                    IssueDTO issueDTO = new IssueDTO();
                    BeanUtils.copyProperties(issue, issueDTO);
                    
                    // 查询记录人信息
                    if (issue.getRecorderId() != null) {
                        User recorder = userMapper.selectById(issue.getRecorderId());
                        if (recorder != null) {
                            issueDTO.setRecorderName(recorder.getRealName());
                        }
                    }
                    
                    // 查询处理人信息
                    if (issue.getHandlerId() != null) {
                        User handler = userMapper.selectById(issue.getHandlerId());
                        if (handler != null) {
                            issueDTO.setHandlerName(handler.getRealName());
                        }
                    }
                    
                    return issueDTO;
                }).collect(Collectors.toList());
            }
            
            // 封装返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("records", issueDTOList);
            result.put("total", issueIPage.getTotal());
            result.put("size", issueIPage.getSize());
            result.put("current", issueIPage.getCurrent());
            result.put("pages", issueIPage.getPages());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取问题列表失败", e);
            return Result.failed("获取问题列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<IssueDTO> getIssueDetail(Long id) {
        try {
            Issue issue = baseMapper.selectById(id);
            if (issue == null || issue.getDeleted() == 1) {
                return Result.failed("问题不存在");
            }
            
            IssueDTO issueDTO = new IssueDTO();
            BeanUtils.copyProperties(issue, issueDTO);
            
            // 查询记录人信息
            if (issue.getRecorderId() != null) {
                User recorder = userMapper.selectById(issue.getRecorderId());
                if (recorder != null) {
                    issueDTO.setRecorderName(recorder.getRealName());
                }
            }
            
            // 查询处理人信息
            if (issue.getHandlerId() != null) {
                User handler = userMapper.selectById(issue.getHandlerId());
                if (handler != null) {
                    issueDTO.setHandlerName(handler.getRealName());
                }
            }
            
            // 由于IssueProcessService中没有getProcessList方法，我们需要自己实现获取处理记录的逻辑
            LambdaQueryWrapper<IssueProcess> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(IssueProcess::getIssueId, id)
                    .orderByDesc(IssueProcess::getCreateTime);
            List<IssueProcess> processList = issueProcessService.list(queryWrapper);
            
            // 转换为DTO
            List<IssueProcessDTO> processRecordDTOs = new ArrayList<>();
            for (IssueProcess process : processList) {
                IssueProcessDTO processDTO = new IssueProcessDTO();
                BeanUtils.copyProperties(process, processDTO);
                
                // 查询处理人信息
                if (process.getProcessorId() != null) {
                    User processor = userMapper.selectById(process.getProcessorId());
                    if (processor != null) {
                        processDTO.setProcessorName(processor.getRealName());
                    }
                }
                
                // 获取图片列表 - 这里需要根据实际情况处理
                // 由于IssueProcess中已经包含了images字段，我们可以直接使用
                processDTO.setImages(process.getImages());
                
                processRecordDTOs.add(processDTO);
            }
            
            issueDTO.setProcessRecords(processRecordDTOs);
            
            return Result.success(issueDTO);
        } catch (Exception e) {
            log.error("获取问题详情失败", e);
            return Result.failed("获取问题详情失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createIssue(IssueDTO issueDTO) {
        try {
            Issue issue = new Issue();
            BeanUtils.copyProperties(issueDTO, issue);
            
            // 设置问题编号
            String issueNo = generateIssueNo();
            issue.setIssueNo(issueNo);
            
            // 设置初始状态
            issue.setStatus("pending");
            
            // 设置创建时间
            issue.setCreateTime(LocalDateTime.now());
            issue.setUpdateTime(LocalDateTime.now());
            
            // 保存问题
            baseMapper.insert(issue);
            
            // 创建问题处理记录
            // TODO: 实现创建问题处理记录的逻辑
            
            return Result.success(issue.getId());
        } catch (Exception e) {
            log.error("创建问题失败", e);
            return Result.failed("创建问题失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateIssue(Long id, IssueDTO issueDTO) {
        try {
            Issue issue = baseMapper.selectById(id);
            if (issue == null || issue.getDeleted() == 1) {
                return Result.failed("问题不存在");
            }
            
            // 更新问题信息
            BeanUtils.copyProperties(issueDTO, issue);
            
            // 设置更新时间
            issue.setUpdateTime(LocalDateTime.now());
            
            // 保存更新
            baseMapper.updateById(issue);
            
            return Result.success(id);
        } catch (Exception e) {
            log.error("更新问题失败", e);
            return Result.failed("更新问题失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteIssue(Long id) {
        try {
            Issue issue = baseMapper.selectById(id);
            if (issue == null || issue.getDeleted() == 1) {
                return Result.failed("问题不存在");
            }
            
            // 逻辑删除
            issue.setDeleted(1);
            issue.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(issue);
            
            return Result.success(id);
        } catch (Exception e) {
            log.error("删除问题失败", e);
            return Result.failed("删除问题失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result processIssue(Long id, Map<String, Object> processData) {
        try {
            Issue issue = baseMapper.selectById(id);
            if (issue == null || issue.getDeleted() == 1) {
                return Result.failed("问题不存在");
            }
            
            // 更新问题状态
            issue.setStatus("processing");
            issue.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(issue);
            
            // 创建问题处理记录
            // TODO: 实现创建问题处理记录的逻辑
            
            return Result.success(id);
        } catch (Exception e) {
            log.error("处理问题失败", e);
            return Result.failed("处理问题失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result closeIssue(Long id, Map<String, Object> closeData) {
        try {
            Issue issue = baseMapper.selectById(id);
            if (issue == null || issue.getDeleted() == 1) {
                return Result.failed("问题不存在");
            }
            
            // 更新问题状态
            issue.setStatus("closed");
            issue.setUpdateTime(LocalDateTime.now());
            issue.setHandleTime(LocalDateTime.now());
            
            // 设置处理结果
            if (closeData.containsKey("handleResult")) {
                issue.setHandleResult((String) closeData.get("handleResult"));
            }
            
            // 保存更新
            baseMapper.updateById(issue);
            
            // 创建问题处理记录
            // TODO: 实现创建问题处理记录的逻辑
            
            return Result.success(id);
        } catch (Exception e) {
            log.error("关闭问题失败", e);
            return Result.failed("关闭问题失败：" + e.getMessage());
        }
    }

    @Override
    public void exportIssues(OutputStream outputStream, String status, String type, LocalDate startDate, LocalDate endDate) throws Exception {
        // TODO: 实现导出问题列表的逻辑
    }

    /**
     * 生成问题编号
     *
     * @return 问题编号
     */
    private String generateIssueNo() {
        // 生成格式：ISS + 年月日 + 3位序号
        LocalDate now = LocalDate.now();
        String dateStr = now.toString().replaceAll("-", "");
        
        // 获取当天最大的问题编号
        String prefix = "ISS" + dateStr;
        Integer maxSeq = baseMapper.selectMaxSeqByPrefix(prefix);
        
        // 序号递增
        int seq = maxSeq == null ? 1 : maxSeq + 1;
        
        // 格式化为3位序号
        return prefix + String.format("%03d", seq);
    }

    /**
     * 获取本周问题列表
     *
     * @return 本周问题列表
     */
    @Override
    public Result<List<IssueDTO>> getWeeklyIssues() {
        try {
            // 计算本周的开始和结束日期
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.minusDays(now.getDayOfWeek().getValue() - 1);
            LocalDate endDate = startDate.plusDays(6);
            
            log.info("本周日期范围: {} 至 {}", startDate, endDate);
            
            // 构建查询条件
            LambdaQueryWrapper<Issue> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Issue::getDeleted, 0)
                    .ge(Issue::getCreateTime, startDate.atStartOfDay())
                    .le(Issue::getCreateTime, endDate.plusDays(1).atStartOfDay())
                    .orderByDesc(Issue::getCreateTime);
            
            // 查询本周问题
            List<Issue> issues = baseMapper.selectList(queryWrapper);
            
            // 转换为DTO
            List<IssueDTO> issueDTOList = issues.stream().map(issue -> {
                IssueDTO issueDTO = new IssueDTO();
                BeanUtils.copyProperties(issue, issueDTO);
                return issueDTO;
            }).collect(Collectors.toList());
            
            return Result.success(issueDTOList);
        } catch (Exception e) {
            log.error("获取本周问题列表失败", e);
            return Result.failed("获取本周问题列表失败：" + e.getMessage());
        }
    }
} 