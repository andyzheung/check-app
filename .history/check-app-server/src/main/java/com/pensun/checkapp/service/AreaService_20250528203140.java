package com.pensun.checkapp.service;

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
     * 根据区域编码查询区域信息
     *
     * @param areaCode 区域编码
     * @return 区域信息
     */
    AreaDTO getAreaByCode(String areaCode);
    
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