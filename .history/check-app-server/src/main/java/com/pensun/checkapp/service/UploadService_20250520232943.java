package com.pensun.checkapp.service;

import com.pensun.checkapp.common.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result upload(MultipartFile file);
    void download(String filename);
} 