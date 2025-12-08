package ru.mai.lighthouse.repository.search_diary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum SearchDiaryQueries {
    CREATE("sql/search_diary/create.sql"),
    GET_BY_ID("sql/search_diary/get_by_id.sql"),
    GET_ALL("sql/search_diary/get_all.sql"),
    GET_BY_TEAM("sql/search_diary/get_by_team.sql"),
    GET_BY_VOLUNTEER("sql/search_diary/get_by_volunteer.sql"),
    UPDATE("sql/search_diary/update.sql"),
    DELETE("sql/search_diary/delete.sql"),
    GET_NEXT_ID("sql/search_diary/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

