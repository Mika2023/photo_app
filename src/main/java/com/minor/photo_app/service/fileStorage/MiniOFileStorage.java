package com.minor.photo_app.service.fileStorage;

import com.minor.photo_app.exception.FileStorageException;
import com.minor.photo_app.properties.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MiniOFileStorage implements FileStorage {

    private static final String PUBLIC_LINK_TO_IMAGE = "%s/%s/%s";

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    @Override
    public String saveFile(MultipartFile file) {
        String fileUrl = buildFileUrl(Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1));

        try (InputStream is = file.getInputStream()){
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileUrl)
                            .stream(is, file.getSize(), (long) -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return buildPublicUrl(fileUrl);
        } catch (IOException e) {
            throw new FileStorageException("Не удалось прочитать файл при добавлении в ФХ, ошибка - " + e.getMessage());
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new FileStorageException("Не удалось загрузить файл в ФХ " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String url) {
        String pathToFile = findPathToFileInnerBucket(url);

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(pathToFile)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new FileStorageException("Не удалось удалить файл из ФХ " + e.getMessage());
        }
    }

    private String buildFileUrl(String fileExt) {
        return String.format(
                "%s.%s",
                UUID.randomUUID(),
                fileExt
        );
    }

    private String buildPublicUrl(String fileName) {
        return String.format(PUBLIC_LINK_TO_IMAGE, minioProperties.getPublicUrl(), minioProperties.getBucketName(), fileName);
    }

    private String findPathToFileInnerBucket(String fileUrl) {
        int indexAfterBucket = fileUrl.indexOf(minioProperties.getBucketName());
        if (indexAfterBucket == -1) {
            throw new IllegalArgumentException("Неверная ссылка на файл из ФХ! Отсутсвует название хранилища - " + fileUrl);
        }
        return fileUrl.substring(indexAfterBucket + minioProperties.getBucketName().length() + 1);
    }
}
