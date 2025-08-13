package com.the.mudpot.controller;

import com.the.mudpot.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + filePath);
    }
}

