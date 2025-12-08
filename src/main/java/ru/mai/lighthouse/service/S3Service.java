package ru.mai.lighthouse.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket:lighthouse-photos}")
    private String bucketName;

    /**
     * Загружает файл в S3 и возвращает URL
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (Objects.isNull(file) || file.isEmpty()) {
            return null;
        }

        String fileName = generateFileName(file.getOriginalFilename());
        String key = String.format("%s/%s", folder, fileName);

        try (InputStream inputStream = file.getInputStream()) {
            S3Resource resource = s3Template.upload(bucketName, key, inputStream);
            log.info("Файл успешно загружен в S3: {}", key);
            return resource.getURL().toString();
        } catch (Exception e) {
            log.error("Ошибка при загрузке файла в S3: {}", e.getMessage(), e);
            throw new IOException("Не удалось загрузить файл в S3", e);
        }
    }

    /**
     * Загружает несколько файлов в S3 и возвращает список URL
     */
    public List<String> uploadFiles(List<MultipartFile> files, String folder) throws IOException {
        List<String> urls = new ArrayList<>();
        if (Objects.isNull(files) || files.isEmpty()) {
            return urls;
        }

        for (MultipartFile file : files) {
            if (Objects.nonNull(file) && !file.isEmpty()) {
                String url = uploadFile(file, folder);
                if (Objects.nonNull(url)) {
                    urls.add(url);
                }
            }
        }

        return urls;
    }

    /**
     * Удаляет файл из S3
     */
    public void deleteFile(String url) {
        if (Objects.isNull(url) || url.isEmpty()) {
            return;
        }

        try {
            String key = extractKeyFromUrl(url);
            s3Template.deleteObject(bucketName, key);
            log.info("Файл успешно удален из S3: {}", key);
        } catch (Exception e) {
            log.error("Ошибка при удалении файла из S3: {}", e.getMessage(), e);
        }
    }

    /**
     * Удаляет несколько файлов из S3
     */
    public void deleteFiles(List<String> urls) {
        if (Objects.isNull(urls) || urls.isEmpty()) {
            return;
        }

        for (String url : urls) {
            deleteFile(url);
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        if (Objects.nonNull(originalFilename) && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private String extractKeyFromUrl(String url) {
        // Извлекаем ключ из URL
        // Формат URL может быть разным в зависимости от конфигурации S3
        if (url.contains("/")) {
            int lastSlashIndex = url.lastIndexOf("/");
            if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
                return url.substring(lastSlashIndex + 1);
            }
        }
        return url;
    }
}

