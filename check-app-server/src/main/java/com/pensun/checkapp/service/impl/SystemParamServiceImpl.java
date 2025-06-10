package com.pensun.checkapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pensun.checkapp.entity.SystemParam;
import com.pensun.checkapp.mapper.SystemParamMapper;
import com.pensun.checkapp.service.SystemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统参数Service实现类
 */
@Slf4j
@Service
public class SystemParamServiceImpl extends ServiceImpl<SystemParamMapper, SystemParam> implements SystemParamService {

    @Override
    public String getParamValue(String paramKey) {
        if (!StringUtils.hasText(paramKey)) {
            return null;
        }
        return baseMapper.selectValueByKey(paramKey);
    }

    @Override
    public String getParamValue(String paramKey, String defaultValue) {
        String value = getParamValue(paramKey);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setParamValue(String paramKey, String paramValue, String paramDesc) {
        if (!StringUtils.hasText(paramKey)) {
            return false;
        }
        
        try {
            // 查询参数是否存在
            LambdaQueryWrapper<SystemParam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemParam::getParamKey, paramKey);
            SystemParam param = baseMapper.selectOne(queryWrapper);
            
            if (param == null) {
                // 新增参数
                param = new SystemParam();
                param.setParamKey(paramKey);
                param.setParamValue(paramValue);
                param.setParamDesc(paramDesc);
                return save(param);
            } else {
                // 更新参数
                param.setParamValue(paramValue);
                if (StringUtils.hasText(paramDesc)) {
                    param.setParamDesc(paramDesc);
                }
                return updateById(param);
            }
        } catch (Exception e) {
            log.error("设置系统参数失败", e);
            return false;
        }
    }

    @Override
    public Map<String, String> getAllParams() {
        List<SystemParam> params = list();
        return params.stream().collect(Collectors.toMap(
                SystemParam::getParamKey,
                SystemParam::getParamValue,
                (v1, v2) -> v1,
                HashMap::new
        ));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByKey(String paramKey) {
        if (!StringUtils.hasText(paramKey)) {
            return false;
        }
        
        try {
            LambdaQueryWrapper<SystemParam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemParam::getParamKey, paramKey);
            return remove(queryWrapper);
        } catch (Exception e) {
            log.error("删除系统参数失败", e);
            return false;
        }
    }
} 