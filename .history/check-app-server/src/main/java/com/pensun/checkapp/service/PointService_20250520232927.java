package com.pensun.checkapp.service;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.dto.PointDTO;

public interface PointService {
    Result list(String keyword, Integer page, Integer size);
    Result getById(Long id);
    Result create(PointDTO pointDTO);
    Result update(Long id, PointDTO pointDTO);
    Result delete(Long id);
} 