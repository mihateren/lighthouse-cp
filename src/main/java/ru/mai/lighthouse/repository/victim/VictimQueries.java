package ru.mai.lighthouse.repository.victim;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum VictimQueries {
    CREATE("sql/victim/create.sql"),
    GET_BY_ID("sql/victim/get_by_id.sql"),
    GET_ALL("sql/victim/get_all.sql"),
    GET_BY_CITY("sql/victim/get_by_city.sql"),
    UPDATE("sql/victim/update.sql"),
    DELETE("sql/victim/delete.sql"),
    GET_NEXT_ID("sql/victim/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

