package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.InspectionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 巡检项Mapper接口
 */
@Mapper
public interface InspectionItemMapper extends BaseMapper<InspectionItem> {

    /**
     * 根据记录ID和类型查询巡检项
     *
     * @param recordId 记录ID
     * @param type     类型
     * @return 巡检项列表
     */
    @Select("SELECT * FROM t_inspection_item WHERE record_id = #{recordId} AND type = #{type} AND deleted = 0")
    List<InspectionItem> selectByRecordIdAndType(@Param("recordId") Long recordId, @Param("type") String type);

    /**
     * 根据记录ID查询异常巡检项数量
     *
     * @param recordId 记录ID
     * @return 异常巡检项数量
     */
    @Select("SELECT COUNT(*) FROM t_inspection_item WHERE record_id = #{recordId} AND status = 'abnormal' AND deleted = 0")
    int countAbnormalItems(@Param("recordId") Long recordId);
} 