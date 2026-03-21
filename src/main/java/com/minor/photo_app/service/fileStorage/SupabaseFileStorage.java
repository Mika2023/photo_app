package com.minor.photo_app.service.fileStorage;

import com.minor.photo_app.dto.response.supabaseResponse.SupabaseResponseDto;
import com.minor.photo_app.exception.FileStorageException;
import com.minor.photo_app.properties.SupabaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class SupabaseFileStorage implements FileStorage {

    private static final String FULL_PATH_TO_FILE_STORAGE = "/storage/v1/object/%s/%s";
    private static final String FULL_PATH_TO_PUBLIC_FILE_STORAGE = "/storage/v1/object/public/%s/%s";
    private static final String PATH_TO_BUCKET = "/storage/v1/object/%s";
    private final SupabaseProperties supabaseProperties;
    private final WebClient supabaseWebClient;

    @Override
    public String saveFile(MultipartFile file) {
        String fileUrl = buildFileUrl(Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1));

        String uriWithFileUrl = getUriWithFileUrl(fileUrl);

        try {
            return supabaseWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(uriWithFileUrl)
                            .build()
                    )
                    .header(HttpHeaders.CONTENT_TYPE, Objects.requireNonNull(file.getContentType()))
                    .header("x-upsert", "true")
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new FileStorageException(
                                            "Не удалось загрузить файл в файловое " +
                                                    "хранилище, ошибка - " + clientResponse.statusCode(),
                                            clientResponse.statusCode()
                                    )
                            )
                    )
                    .bodyToMono(SupabaseResponseDto.class)
                    .onErrorResume(e ->
                            Mono.error(new FileStorageException("Не удалось выполнить запрос к ФХ, ошибка - " + e.getMessage()))
                    )
                    .map(supabaseResponseDto -> getPublicUriWithFileUrl(fileUrl))
                    .block();
        } catch (IOException e) {
            throw new FileStorageException("Не удалось выполнить запрос к ФХ, ошибка - " + e.getMessage());
        }
    }

    private String getUriWithFileUrl(String fileUrl) {
        return String.format(FULL_PATH_TO_FILE_STORAGE, supabaseProperties.getBucketName(), fileUrl);
    }

    private String getPublicUriWithFileUrl(String fileUrl) {
        return supabaseProperties.getBaseUrl() + String.format(FULL_PATH_TO_PUBLIC_FILE_STORAGE, supabaseProperties.getBucketName(), fileUrl);
    }

    @Override
    public void deleteFile(String url) {
        String pathToFile = findPathToFileInnerBucket(url);
        String pathToBucket = buildPathToBucket();
        Map<String, Object> requestBody = Map.of(
                "prefixes", List.of(pathToFile)
        );

        supabaseWebClient.method(HttpMethod.DELETE)
                .uri(pathToBucket)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new FileStorageException(
                                        "Не удалось удалить файл из файлового " +
                                                "хранилища, ошибка - " + clientResponse.statusCode(),
                                        clientResponse.statusCode()
                                )
                        )
                )
                .toBodilessEntity()
                .block();
    }

    private String buildFileUrl(String fileExt) {
        return String.format(
                "%s.%s",
                UUID.randomUUID(),
                fileExt
        );
    }

    private String buildPathToBucket() {
        return String.format(PATH_TO_BUCKET, supabaseProperties.getBucketName());
    }

    private String findPathToFileInnerBucket(String fileUrl) {
        int indexAfterBucket = fileUrl.indexOf(supabaseProperties.getBucketName());
        if (indexAfterBucket == -1) {
            throw new IllegalArgumentException("Неверная ссылка на файл из ФХ! Отсутсвует название хранилища - " + fileUrl);
        }
        return fileUrl.substring(indexAfterBucket + supabaseProperties.getBucketName().length() + 1);
    }
}
