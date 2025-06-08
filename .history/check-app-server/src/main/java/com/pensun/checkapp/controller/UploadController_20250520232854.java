package com.pensun.checkapp.controller;

import com.pensun.checkapp.common.Result;
import com.pensun.checkapp.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file) {
        return uploadService.upload(file);
    }

    @GetMapping("/{filename}")
    public void download(@PathVariable String filename) {
        uploadService.download(filename);
    }
} 