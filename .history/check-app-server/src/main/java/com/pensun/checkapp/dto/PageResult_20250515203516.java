package com.pensun.checkapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果DTO
 */
@Data
@Accessors(chain = true)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页数据
     */
    private List<T> list;

    /**
     * 创建分页结果
     *
     * @param list  数据列表
     * @param total 总记录数
     * @param <T>   数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> list, long total) {
        return new PageResult<T>()
                .setList(list)
                .setTotal(total);
    }
} 