package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 区域Mapper接口
 */
@Mapper
public interface AreaMapper extends BaseMapper<Area> {

    /**
     * 根据区域编码查询区域信息
     * @param areaCode 区域编码
     * @return 区域实体
     */
    @Select("SELECT * FROM t_area WHERE code = #{areaCode} AND deleted = 0")
    Area selectByAreaCode(@Param("areaCode") String areaCode);

    /**
     * 根据区域ID查询巡检项名称列表
     *
     * @param areaId 区域ID
     * @return 巡检项名称列表
     */
    @Select("SELECT name FROM t_area_check_item WHERE area_id = #{areaId} AND deleted = 0 ORDER BY sort ASC")
    List<String> selectCheckItemNamesByAreaId(@Param("areaId") Long areaId);
} 