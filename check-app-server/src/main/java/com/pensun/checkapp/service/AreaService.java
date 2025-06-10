package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.AreaDTO;
import java.util.List;

/**
 * 区域服务接口
 */
public interface AreaService {
    /**
     * 获取所有区域列表
     *
     * @return 区域列表
     */
    List<AreaDTO> getAllAreas();
    
    /**
     * 获取区域列表（带过滤条件）
     *
     * @param status 状态
     * @param type 区域类型
     * @param keyword 关键字
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    Result<Page<AreaDTO>> getAllAreas(String status, String type, String keyword, Integer page, Integer size);

    /**
     * 根据区域编码查询区域信息
     *
     * @param areaCode 区域编码
     * @return 区域信息
     */
    AreaDTO getAreaByCode(String areaCode);
    
    /**
     * 获取区域二维码
     *
     * @param id 区域ID
     * @return 二维码URL
     */
    String getQRCode(Long id);
    
    /**
     * 生成区域二维码
     *
     * @param id 区域ID
     * @return 二维码URL
     */
    String generateQRCode(Long id);
    
    /**
     * 验证区域二维码
     *
     * @param qrData 二维码数据
     * @return 验证结果
     */
    Boolean verifyQRCode(String qrData);
} 