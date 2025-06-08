package com.pensun.checkapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 部门实体类
 */
@Data
@Accessors(chain = true)
@TableName("t_department")
public class Department {
    
    /**
     * 部门ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 部门名称
     */
    private String name;
    
    /**
     * 部门编码
     */
    private String code;
    
    /**
     * 父部门ID
     */
    private Long parentId;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 