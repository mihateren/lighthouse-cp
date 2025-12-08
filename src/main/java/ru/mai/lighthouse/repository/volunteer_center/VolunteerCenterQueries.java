package ru.mai.lighthouse.repository.volunteer_center;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum VolunteerCenterQueries {
    CREATE("sql/volunteer_center/create.sql"),
    GET_BY_ID("sql/volunteer_center/get_by_id.sql"),
    GET_ALL("sql/volunteer_center/get_all.sql"),
    UPDATE("sql/volunteer_center/update.sql"),
    DELETE("sql/volunteer_center/delete.sql"),
    GET_NEXT_ID("sql/volunteer_center/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

