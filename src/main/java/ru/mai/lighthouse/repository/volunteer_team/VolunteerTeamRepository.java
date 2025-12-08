package ru.mai.lighthouse.repository.volunteer_team;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.team.TeamMemberRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamResponse;
import ru.mai.lighthouse.entity.domain.VolunteerTeam;
import ru.mai.lighthouse.mapper.VolunteerTeamMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VolunteerTeamRepository {

    // SQL параметры в snake_case
    private static final String PARAM_TEAM_ID = "team_id";
    private static final String PARAM_OPERATION_ID = "operation_id";
    private static final String PARAM_TEAM_LEADER_ID = "team_leader_id";
    private static final String PARAM_TEAM_NAME = "team_name";
    private static final String PARAM_VOLUNTEER_ID = "volunteer_id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final VolunteerTeamMapper volunteerTeamMapper = VolunteerTeamMapper.INSTANCE;

    private VolunteerTeam mapFromResultSet(ResultSet rs) throws SQLException {
        return new VolunteerTeam()
                .setTeamId(rs.getLong("team_id"))
                .setOperationId(rs.getLong("operation_id"))
                .setTeamLeaderId(rs.getLong("team_leader_id"))
                .setTeamName(rs.getString("team_name"))
                .setCreatedAt(Objects.nonNull(rs.getTimestamp("created_at"))
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null)
                .setUpdatedAt(Objects.nonNull(rs.getTimestamp("updated_at"))
                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                VolunteerTeamQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public VolunteerTeamResponse create(VolunteerTeamRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = volunteerTeamMapper.toParams(request, id);

        VolunteerTeam team = namedParameterJdbcTemplate.query(
                VolunteerTeamQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return volunteerTeamMapper.toResponse(team);
    }

    public Optional<VolunteerTeamResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_TEAM_ID, id);
        try {
            VolunteerTeam team = namedParameterJdbcTemplate.query(
                    VolunteerTeamQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(team).map(volunteerTeamMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<VolunteerTeamResponse> getAll() {
        List<VolunteerTeam> teams = namedParameterJdbcTemplate.query(
                VolunteerTeamQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return teams.stream().map(volunteerTeamMapper::toResponse).toList();
    }

    public List<VolunteerTeamResponse> getByOperation(Long operationId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_OPERATION_ID, operationId);
        List<VolunteerTeam> teams = namedParameterJdbcTemplate.query(
                VolunteerTeamQueries.GET_BY_OPERATION.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return teams.stream().map(volunteerTeamMapper::toResponse).toList();
    }

    @Transactional
    public VolunteerTeamResponse update(Long id, VolunteerTeamRequest request) {
        MapSqlParameterSource params = volunteerTeamMapper.toParams(request, id);
        VolunteerTeam team = namedParameterJdbcTemplate.query(
                VolunteerTeamQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return volunteerTeamMapper.toResponse(team);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_TEAM_ID, id);
        namedParameterJdbcTemplate.update(
                VolunteerTeamQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public void addMember(Long teamId, TeamMemberRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_TEAM_ID, teamId)
                .addValue(PARAM_VOLUNTEER_ID, request.getVolunteerId());
        namedParameterJdbcTemplate.update(
                VolunteerTeamQueries.ADD_MEMBER.getSql(),
                params
        );
    }

    @Transactional
    public void removeMember(Long teamId, Long volunteerId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_TEAM_ID, teamId)
                .addValue(PARAM_VOLUNTEER_ID, volunteerId);
        namedParameterJdbcTemplate.update(
                VolunteerTeamQueries.REMOVE_MEMBER.getSql(),
                params
        );
    }

    public List<Long> getTeamMembers(Long teamId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_TEAM_ID, teamId);
        return namedParameterJdbcTemplate.queryForList(
                VolunteerTeamQueries.GET_TEAM_MEMBERS.getSql(),
                params,
                Long.class
        );
    }

    @Transactional
    public List<VolunteerTeamResponse> batchInsert(List<VolunteerTeamRequest> requests) {
        List<VolunteerTeamResponse> results = new java.util.ArrayList<>();
        for (VolunteerTeamRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<VolunteerTeamResponse> batchUpdate(List<VolunteerTeamRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<VolunteerTeamResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
