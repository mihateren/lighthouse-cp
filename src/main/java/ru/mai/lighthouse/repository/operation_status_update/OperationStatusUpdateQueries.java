package ru.mai.lighthouse.repository.operation_status_update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum OperationStatusUpdateQueries {
    CREATE("sql/operation_status_update/create.sql"),
    GET_BY_ID("sql/operation_status_update/get_by_id.sql"),
    GET_ALL("sql/operation_status_update/get_all.sql"),
    GET_BY_OPERATION("sql/operation_status_update/get_by_operation.sql"),
    GET_PENDING("sql/operation_status_update/get_pending.sql"),
    UPDATE_DECISION("sql/operation_status_update/update_decision.sql"),
    UPDATE_OPERATION_STATUS("sql/operation_status_update/update_operation_status.sql"),
    GET_NEXT_ID("sql/operation_status_update/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

