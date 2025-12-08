package ru.mai.lighthouse.repository.volunteer_team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mai.lighthouse.repository.SqlLoader;

@Getter
@RequiredArgsConstructor
public enum VolunteerTeamQueries {
    CREATE("sql/volunteer_team/create.sql"),
    GET_BY_ID("sql/volunteer_team/get_by_id.sql"),
    GET_ALL("sql/volunteer_team/get_all.sql"),
    GET_BY_OPERATION("sql/volunteer_team/get_by_operation.sql"),
    UPDATE("sql/volunteer_team/update.sql"),
    DELETE("sql/volunteer_team/delete.sql"),
    ADD_MEMBER("sql/volunteer_team/add_member.sql"),
    REMOVE_MEMBER("sql/volunteer_team/remove_member.sql"),
    GET_TEAM_MEMBERS("sql/volunteer_team/get_team_members.sql"),
    GET_NEXT_ID("sql/volunteer_team/get_next_id.sql");

    private final String filePath;

    public String getSql() {
        return SqlLoader.loadSql(filePath);
    }

    public java.nio.file.Path getPath() {
        return java.nio.file.Paths.get(filePath);
    }
}

