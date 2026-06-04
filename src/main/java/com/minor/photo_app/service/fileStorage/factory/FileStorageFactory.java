package com.minor.photo_app.service.fileStorage.factory;

import com.minor.photo_app.properties.MinioProperties;
import com.minor.photo_app.properties.SupabaseProperties;
import com.minor.photo_app.service.fileStorage.FileStorage;
import com.minor.photo_app.service.fileStorage.LocalFileStorage;
import com.minor.photo_app.service.fileStorage.MiniOFileStorage;
import com.minor.photo_app.service.fileStorage.SupabaseFileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileStorageFactory {
    private final LocalFileStorage localFileStorage;
    private final MiniOFileStorage miniOFileStorage;
    private final SupabaseFileStorage supabaseFileStorage;

    private final SupabaseProperties supabaseProperties;
    private final MinioProperties minioProperties;

    public FileStorage getFileStorageByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Ошибка получения файлового хранилища в фабрике. Ссылка на файл пуста");
        }

        if (url.startsWith(supabaseProperties.getBaseUrl())) {
            return supabaseFileStorage;
        }

        if (url.startsWith(minioProperties.getUrl())) {
            return miniOFileStorage;
        }

        return localFileStorage;
    }

    public FileStorage getFileStorageToSave() {
        return miniOFileStorage;
    }
}
