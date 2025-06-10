package com.pensun.checkapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pensun.checkapp.entity.FileUpload;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传Mapper接口
 */
@Mapper
public interface FileUploadMapper extends BaseMapper<FileUpload> {
} 