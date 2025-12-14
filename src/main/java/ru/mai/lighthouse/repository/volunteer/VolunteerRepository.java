package ru.mai.lighthouse.repository.volunteer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerRequest;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerResponse;
import ru.mai.lighthouse.entity.domain.Volunteer;
import ru.mai.lighthouse.mapper.VolunteerMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VolunteerRepository {

    // SQL параметры в snake_case
    private static final String PARAM_VOLUNTEER_ID = "volunteer_id";
    private static final String PARAM_CENTER_ID = "center_id";
    private static final String PARAM_FIRST_NAME = "first_name";
    private static final String PARAM_LAST_NAME = "last_name";
    private static final String PARAM_BIRTHDATE = "birthdate";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_SKILLS = "skills";
    private static final String PARAM_IS_ACTIVE = "is_active";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final VolunteerMapper volunteerMapper;

    private Volunteer mapFromResultSet(ResultSet rs) throws SQLException {
        return new Volunteer()
                .setVolunteerId(rs.getLong("volunteer_id"))
                .setCenterId(rs.getLong("center_id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setBirthdate(Objects.nonNull(rs.getDate("birthdate"))
                    ? rs.getDate("birthdate").toLocalDate()
                    : null)
                .setPhone(rs.getString("phone"))
                .setEmail(rs.getString("email"))
                .setSkills(rs.getString("skills"))
                .setIsActive(rs.getBoolean("is_active"))
                .setRegistrationDate(Objects.nonNull(rs.getDate("registration_date"))
                    ? rs.getDate("registration_date").toLocalDate()
                    : null)
                .setUpdatedAt(Objects.nonNull(rs.getTimestamp("updated_at"))
                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                VolunteerQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public VolunteerResponse create(VolunteerRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = volunteerMapper.toParams(request, id);

        Volunteer volunteer = namedParameterJdbcTemplate.query(
                VolunteerQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return volunteerMapper.toResponse(volunteer);
    }

    public Optional<VolunteerResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VOLUNTEER_ID, id);
        try {
            Volunteer volunteer = namedParameterJdbcTemplate.query(
                    VolunteerQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(volunteer).map(volunteerMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<VolunteerResponse> getAll() {
        List<Volunteer> volunteers = namedParameterJdbcTemplate.query(
                VolunteerQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return volunteers.stream().map(volunteerMapper::toResponse).toList();
    }

    public List<VolunteerResponse> getByCenter(Long centerId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_CENTER_ID, centerId);
        List<Volunteer> volunteers = namedParameterJdbcTemplate.query(
                VolunteerQueries.GET_BY_CENTER.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return volunteers.stream().map(volunteerMapper::toResponse).toList();
    }

    public List<VolunteerResponse> getActive() {
        List<Volunteer> volunteers = namedParameterJdbcTemplate.query(
                VolunteerQueries.GET_ACTIVE.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return volunteers.stream().map(volunteerMapper::toResponse).toList();
    }

    @Transactional
    public VolunteerResponse update(Long id, VolunteerRequest request) {
        MapSqlParameterSource params = volunteerMapper.toParams(request, id);
        Volunteer volunteer = namedParameterJdbcTemplate.query(
                VolunteerQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return volunteerMapper.toResponse(volunteer);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VOLUNTEER_ID, id);
        namedParameterJdbcTemplate.update(
                VolunteerQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public List<VolunteerResponse> batchInsert(List<VolunteerRequest> requests) {
        List<VolunteerResponse> results = new java.util.ArrayList<>();
        for (VolunteerRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<VolunteerResponse> batchUpdate(List<VolunteerRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<VolunteerResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
