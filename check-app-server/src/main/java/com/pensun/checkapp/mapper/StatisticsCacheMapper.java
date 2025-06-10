package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.StatisticsCache;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * 统计数据缓存Mapper接口
 */
@Mapper
public interface StatisticsCacheMapper extends BaseMapper<StatisticsCache> {
    
    /**
     * 根据缓存键查询缓存数据
     *
     * @param cacheKey 缓存键
     * @return 缓存数据
     */
    @Select("SELECT cache_data FROM t_statistics_cache WHERE cache_key = #{cacheKey} AND expire_time > NOW() LIMIT 1")
    String selectDataByKey(@Param("cacheKey") String cacheKey);
    
    /**
     * 更新缓存数据和过期时间
     *
     * @param cacheKey 缓存键
     * @param cacheData 缓存数据
     * @param expireTime 过期时间
     * @return 影响行数
     */
    @Update("UPDATE t_statistics_cache SET cache_data = #{cacheData}, expire_time = #{expireTime}, update_time = NOW() WHERE cache_key = #{cacheKey}")
    int updateCache(@Param("cacheKey") String cacheKey, @Param("cacheData") String cacheData, @Param("expireTime") LocalDateTime expireTime);
} 