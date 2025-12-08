package ru.mai.lighthouse.repository.search_operation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum SearchOperationQueries {
    CREATE("sql/search_operation/create.sql"),
    GET_BY_ID("sql/search_operation/get_by_id.sql"),
    GET_ALL("sql/search_operation/get_all.sql"),
    GET_BY_STATUS("sql/search_operation/get_by_status.sql"),
    GET_BY_VICTIM("sql/search_operation/get_by_victim.sql"),
    GET_BY_ADMIN("sql/search_operation/get_by_admin.sql"),
    UPDATE("sql/search_operation/update.sql"),
    DELETE("sql/search_operation/delete.sql"),
    UPDATE_OPERATION_STATUS("sql/search_operation/update_operation_status.sql"),
    GET_NEXT_ID("sql/search_operation/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

