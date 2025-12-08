package ru.mai.lighthouse.repository.volunteer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum VolunteerQueries {
    CREATE("sql/volunteer/create.sql"),
    GET_BY_ID("sql/volunteer/get_by_id.sql"),
    GET_ALL("sql/volunteer/get_all.sql"),
    GET_BY_CENTER("sql/volunteer/get_by_center.sql"),
    GET_ACTIVE("sql/volunteer/get_active.sql"),
    UPDATE("sql/volunteer/update.sql"),
    DELETE("sql/volunteer/delete.sql"),
    GET_NEXT_ID("sql/volunteer/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

