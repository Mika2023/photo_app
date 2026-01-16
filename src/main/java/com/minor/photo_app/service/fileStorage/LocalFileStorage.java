package com.minor.photo_app.service.fileStorage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    @Value("${storage.upload-dir}")
    private String storageDirectory;

    @Override
    public String saveFile(MultipartFile file) {
        try {
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "." + ext;

            Path dir = Paths.get(storageDirectory);
            Files.createDirectories(dir);

            Path path = dir.resolve(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }
    }

    @Override
    public void deleteFile(String url) {
        try {
            Path path = Paths.get(storageDirectory + "/" + url);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
    }
}
