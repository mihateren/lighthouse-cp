package ru.mai.lighthouse.repository.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.admin.AdminRequest;
import ru.mai.lighthouse.entity.api.admin.AdminResponse;
import ru.mai.lighthouse.entity.domain.Admin;
import ru.mai.lighthouse.mapper.AdminMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    // SQL параметры в snake_case
    private static final String PARAM_ADMIN_ID = "admin_id";
    private static final String PARAM_CENTER_ID = "center_id";
    private static final String PARAM_FIRST_NAME = "first_name";
    private static final String PARAM_LAST_NAME = "last_name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_BIRTHDATE = "birthdate";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final AdminMapper adminMapper = AdminMapper.INSTANCE;

    private Admin mapFromResultSet(ResultSet rs) throws SQLException {
        return new Admin()
                .setAdminId(rs.getLong("admin_id"))
                .setCenterId(rs.getLong("center_id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setEmail(rs.getString("email"))
                .setBirthdate(Objects.nonNull(rs.getDate("birthdate"))
                    ? rs.getDate("birthdate").toLocalDate()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                AdminQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public AdminResponse create(AdminRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = adminMapper.toParams(request, id);

        Admin admin = namedParameterJdbcTemplate.query(
                AdminQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return adminMapper.toResponse(admin);
    }

    public Optional<AdminResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_ADMIN_ID, id);
        try {
            Admin admin = namedParameterJdbcTemplate.query(
                    AdminQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(admin).map(adminMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AdminResponse> getAll() {
        List<Admin> admins = namedParameterJdbcTemplate.query(
                AdminQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return admins.stream().map(adminMapper::toResponse).toList();
    }

    public List<AdminResponse> getByCenter(Long centerId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_CENTER_ID, centerId);
        List<Admin> admins = namedParameterJdbcTemplate.query(
                AdminQueries.GET_BY_CENTER.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return admins.stream().map(adminMapper::toResponse).toList();
    }

    @Transactional
    public AdminResponse update(Long id, AdminRequest request) {
        MapSqlParameterSource params = adminMapper.toParams(request, id);
        Admin admin = namedParameterJdbcTemplate.query(
                AdminQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return adminMapper.toResponse(admin);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_ADMIN_ID, id);
        namedParameterJdbcTemplate.update(
                AdminQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public List<AdminResponse> batchInsert(List<AdminRequest> requests) {
        List<AdminResponse> results = new java.util.ArrayList<>();
        for (AdminRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<AdminResponse> batchUpdate(List<AdminRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<AdminResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
