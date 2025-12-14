package ru.mai.lighthouse.repository.volunteer_center;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.center.CenterResponse;
import ru.mai.lighthouse.entity.api.center.CreateCenterRequest;
import ru.mai.lighthouse.entity.api.center.UpdateCenterRequest;
import ru.mai.lighthouse.entity.domain.VolunteerCenter;
import ru.mai.lighthouse.mapper.VolunteerCenterMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VolunteerCenterRepository {

    // SQL параметры в snake_case
    private static final String PARAM_CENTER_ID = "center_id";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_CITY = "city";
    private static final String PARAM_POST_CODE = "post_code";
    private static final String PARAM_ADDRESS = "address";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final VolunteerCenterMapper volunteerCenterMapper;

    private VolunteerCenter mapFromResultSet(ResultSet rs) throws SQLException {
        return new VolunteerCenter()
                .setCenterId(rs.getLong("center_id"))
                .setTitle(rs.getString("title"))
                .setCity(rs.getString("city"))
                .setPostCode(rs.getString("post_code"))
                .setAddress(rs.getString("address"));
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                VolunteerCenterQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public CenterResponse create(CreateCenterRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = volunteerCenterMapper.toParams(request, id);

        VolunteerCenter center = namedParameterJdbcTemplate.query(
                VolunteerCenterQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return volunteerCenterMapper.toResponse(center);
    }

    public Optional<CenterResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_CENTER_ID, id);
        try {
            VolunteerCenter center = namedParameterJdbcTemplate.query(
                    VolunteerCenterQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(center).map(volunteerCenterMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CenterResponse> getAll() {
        List<VolunteerCenter> centers = namedParameterJdbcTemplate.query(
                VolunteerCenterQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return centers.stream().map(volunteerCenterMapper::toResponse).toList();
    }

    @Transactional
    public CenterResponse update(Long id, UpdateCenterRequest request) {
        MapSqlParameterSource params = volunteerCenterMapper.toParams(request, id);
        VolunteerCenter center = namedParameterJdbcTemplate.query(
                VolunteerCenterQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return volunteerCenterMapper.toResponse(center);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_CENTER_ID, id);
        namedParameterJdbcTemplate.update(
                VolunteerCenterQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public List<CenterResponse> batchInsert(List<CreateCenterRequest> requests) {
        List<CenterResponse> results = new java.util.ArrayList<>();
        for (CreateCenterRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<CenterResponse> batchUpdate(List<UpdateCenterRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<CenterResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
