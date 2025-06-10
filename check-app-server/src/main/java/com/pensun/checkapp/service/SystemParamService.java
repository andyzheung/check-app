package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.entity.SystemParam;

import java.util.Map;

/**
 * 系统参数Service接口
 */
public interface SystemParamService extends IService<SystemParam> {
    
    /**
     * 获取参数值
     *
     * @param paramKey 参数键
     * @return 参数值
     */
    String getParamValue(String paramKey);
    
    /**
     * 获取参数值（带默认值）
     *
     * @param paramKey 参数键
     * @param defaultValue 默认值
     * @return 参数值
     */
    String getParamValue(String paramKey, String defaultValue);
    
    /**
     * 设置参数值
     *
     * @param paramKey 参数键
     * @param paramValue 参数值
     * @param paramDesc 参数描述
     * @return 是否成功
     */
    boolean setParamValue(String paramKey, String paramValue, String paramDesc);
    
    /**
     * 获取所有系统参数
     *
     * @return 参数Map
     */
    Map<String, String> getAllParams();
    
    /**
     * 根据键删除参数
     *
     * @param paramKey 参数键
     * @return 是否成功
     */
    boolean removeByKey(String paramKey);
} 