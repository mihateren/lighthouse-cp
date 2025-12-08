package ru.mai.lighthouse.repository.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum AdminQueries {
    CREATE("sql/admin/create.sql"),
    GET_BY_ID("sql/admin/get_by_id.sql"),
    GET_ALL("sql/admin/get_all.sql"),
    GET_BY_CENTER("sql/admin/get_by_center.sql"),
    UPDATE("sql/admin/update.sql"),
    DELETE("sql/admin/delete.sql"),
    GET_NEXT_ID("sql/admin/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

