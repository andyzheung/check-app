package com.pensun.checkapp.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页结果DTO
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;

    public PageResult(List<T> list, long total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> of(List<T> list, long total) {
        return new PageResult<>(list, total, 1, list.size());
    }
} 