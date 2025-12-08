package ru.mai.lighthouse.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SqlLoader {
    private static final Map<String, String> sqlCache = new HashMap<>();

    public static String loadSql(String path) {
        return sqlCache.computeIfAbsent(path, p -> {
            try {
                ClassPathResource resource = new ClassPathResource(p);
                String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                log.debug("Загружен SQL из файла: {}", p);
                return sql;
            } catch (IOException e) {
                log.error("Ошибка при загрузке SQL из файла: {}", p, e);
                throw new RuntimeException(String.format("Не удалось загрузить SQL из файла: %s", p), e);
            }
        });
    }
}

