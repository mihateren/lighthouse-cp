package ru.mai.lighthouse.repository.victim;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.victim.VictimRequest;
import ru.mai.lighthouse.entity.api.victim.VictimResponse;
import ru.mai.lighthouse.entity.domain.Victim;
import ru.mai.lighthouse.mapper.VictimMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VictimRepository {

    // SQL параметры в snake_case
    private static final String PARAM_VICTIM_ID = "victim_id";
    private static final String PARAM_FIRST_NAME = "first_name";
    private static final String PARAM_LAST_NAME = "last_name";
    private static final String PARAM_CITY = "city";
    private static final String PARAM_AGE = "age";
    private static final String PARAM_HEIGHT = "height";
    private static final String PARAM_PHOTO = "photo";
    private static final String PARAM_NOTES = "notes";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final VictimMapper victimMapper = VictimMapper.INSTANCE;

    private Victim mapFromResultSet(ResultSet rs) throws SQLException {
        Victim victim = new Victim()
                .setVictimId(rs.getLong("victim_id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setCity(rs.getString("city"))
                .setAge(rs.getObject("age", Integer.class))
                .setHeight(rs.getObject("height", Integer.class))
                .setPhoto(rs.getString("photo"))
                .setNotes(rs.getString("notes"))
                .setUpdatedAt(Objects.nonNull(rs.getTimestamp("updated_at"))
                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                    : null);
        try {
            victim.setCreatedAt(Objects.nonNull(rs.getTimestamp("created_at"))
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null);
        } catch (SQLException e) {
            // created_at может отсутствовать в некоторых запросах
        }
        return victim;
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                VictimQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public VictimResponse create(VictimRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = victimMapper.toParams(request, id);

        Victim victim = namedParameterJdbcTemplate.query(
                VictimQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return victimMapper.toResponse(victim);
    }

    public Optional<VictimResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VICTIM_ID, id);
        try {
            Victim victim = namedParameterJdbcTemplate.query(
                    VictimQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(victim).map(victimMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<VictimResponse> getAll() {
        List<Victim> victims = namedParameterJdbcTemplate.query(
                VictimQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return victims.stream().map(victimMapper::toResponse).toList();
    }

    public List<VictimResponse> getByCity(String city) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_CITY, city);
        List<Victim> victims = namedParameterJdbcTemplate.query(
                VictimQueries.GET_BY_CITY.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return victims.stream().map(victimMapper::toResponse).toList();
    }

    @Transactional
    public VictimResponse update(Long id, VictimRequest request) {
        MapSqlParameterSource params = victimMapper.toParams(request, id);
        Victim victim = namedParameterJdbcTemplate.query(
                VictimQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return victimMapper.toResponse(victim);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VICTIM_ID, id);
        namedParameterJdbcTemplate.update(
                VictimQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public List<VictimResponse> batchInsert(List<VictimRequest> requests) {
        List<VictimResponse> results = new java.util.ArrayList<>();
        for (VictimRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<VictimResponse> batchUpdate(List<VictimRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<VictimResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
