package ru.mai.lighthouse.repository.search_diary;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryRequest;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryResponse;
import ru.mai.lighthouse.entity.domain.SearchDiary;
import ru.mai.lighthouse.mapper.AdminMapper;
import ru.mai.lighthouse.mapper.SearchDiaryMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchDiaryRepository {

    // SQL параметры в snake_case
    private static final String PARAM_DIARY_ID = "diary_id";
    private static final String PARAM_TEAM_ID = "team_id";
    private static final String PARAM_VOLUNTEER_ID = "volunteer_id";
    private static final String PARAM_ACTIVITY_TYPE = "activity_type";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_LOCATION = "location";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SearchDiaryMapper searchDiaryMapper;

    private SearchDiary mapFromResultSet(ResultSet rs) throws SQLException {
        return new SearchDiary()
                .setDiaryId(rs.getLong("diary_id"))
                .setTeamId(rs.getLong("team_id"))
                .setVolunteerId(rs.getLong("volunteer_id"))
                .setActivityType(rs.getString("activity_type"))
                .setDescription(rs.getString("description"))
                .setLocation(rs.getString("location"))
                .setCreatedAt(Objects.nonNull(rs.getTimestamp("created_at"))
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                SearchDiaryQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public SearchDiaryResponse create(SearchDiaryRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = searchDiaryMapper.toParams(request, id);

        SearchDiary diary = namedParameterJdbcTemplate.query(
                SearchDiaryQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return searchDiaryMapper.toResponse(diary);
    }

    public Optional<SearchDiaryResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_DIARY_ID, id);
        try {
            SearchDiary diary = namedParameterJdbcTemplate.query(
                    SearchDiaryQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(diary).map(searchDiaryMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<SearchDiaryResponse> getAll() {
        List<SearchDiary> diaries = namedParameterJdbcTemplate.query(
                SearchDiaryQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return diaries.stream().map(searchDiaryMapper::toResponse).toList();
    }

    public List<SearchDiaryResponse> getByTeam(Long teamId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_TEAM_ID, teamId);
        List<SearchDiary> diaries = namedParameterJdbcTemplate.query(
                SearchDiaryQueries.GET_BY_TEAM.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return diaries.stream().map(searchDiaryMapper::toResponse).toList();
    }

    public List<SearchDiaryResponse> getByVolunteer(Long volunteerId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VOLUNTEER_ID, volunteerId);
        List<SearchDiary> diaries = namedParameterJdbcTemplate.query(
                SearchDiaryQueries.GET_BY_VOLUNTEER.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return diaries.stream().map(searchDiaryMapper::toResponse).toList();
    }

    @Transactional
    public SearchDiaryResponse update(Long id, SearchDiaryRequest request) {
        MapSqlParameterSource params = searchDiaryMapper.toParams(request, id);
        SearchDiary diary = namedParameterJdbcTemplate.query(
                SearchDiaryQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return searchDiaryMapper.toResponse(diary);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_DIARY_ID, id);
        namedParameterJdbcTemplate.update(
                SearchDiaryQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public List<SearchDiaryResponse> batchInsert(List<SearchDiaryRequest> requests) {
        List<SearchDiaryResponse> results = new java.util.ArrayList<>();
        for (SearchDiaryRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<SearchDiaryResponse> batchUpdate(List<SearchDiaryRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<SearchDiaryResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
