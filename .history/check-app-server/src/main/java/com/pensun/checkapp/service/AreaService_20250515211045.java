package com.pensun.checkapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pensun.checkapp.dto.AreaDTO;
import com.pensun.checkapp.dto.PageResult;
import com.pensun.checkapp.entity.Area;

/**
 * 区域Service接口
 */
public interface AreaService extends IService<Area> {

    /**
     * 分页查询区域列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param name     区域名称
     * @param status   状态
     * @return 分页结果
     */
    PageResult<AreaDTO> getAreaList(int page, int pageSize, String name, String status);

    /**
     * 获取区域详情
     *
     * @param id 区域ID
     * @return 区域详情
     */
    AreaDTO getAreaDetail(Long id);

    /**
     * 添加区域
     *
     * @param areaDTO 区域DTO
     * @return 是否成功
     */
    boolean addArea(AreaDTO areaDTO);

    /**
     * 更新区域
     *
     * @param id      区域ID
     * @param areaDTO 区域DTO
     * @return 是否成功
     */
    boolean updateArea(Long id, AreaDTO areaDTO);

    /**
     * 删除区域
     *
     * @param id 区域ID
     * @return 是否成功
     */
    boolean deleteArea(Long id);

    /**
     * 获取区域二维码
     *
     * @param id 区域ID
     * @return 二维码图片字节数组
     */
    byte[] getAreaQrCode(Long id);
} 