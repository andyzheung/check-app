package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.SystemParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统参数Mapper接口
 */
@Mapper
public interface SystemParamMapper extends BaseMapper<SystemParam> {
    
    /**
     * 根据参数键查询参数值
     *
     * @param paramKey 参数键
     * @return 参数值
     */
    @Select("SELECT param_value FROM t_system_param WHERE param_key = #{paramKey} LIMIT 1")
    String selectValueByKey(@Param("paramKey") String paramKey);
} 