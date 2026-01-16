package com.minor.photo_app.service.fileStorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    String saveFile(MultipartFile file);
    void deleteFile(String url);
}
