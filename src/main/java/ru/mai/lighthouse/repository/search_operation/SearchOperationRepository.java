package ru.mai.lighthouse.repository.search_operation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.operation.SearchOperationRequest;
import ru.mai.lighthouse.entity.api.operation.SearchOperationResponse;
import ru.mai.lighthouse.entity.domain.SearchOperation;
import ru.mai.lighthouse.mapper.SearchOperationMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchOperationRepository {

    // SQL параметры в snake_case
    private static final String PARAM_OPERATION_ID = "operation_id";
    private static final String PARAM_VICTIM_ID = "victim_id";
    private static final String PARAM_ADMIN_ID = "admin_id";
    private static final String PARAM_STATUS = "status";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SearchOperationMapper searchOperationMapper = SearchOperationMapper.INSTANCE;

    private SearchOperation mapFromResultSet(ResultSet rs) throws SQLException {
        return new SearchOperation()
                .setOperationId(rs.getLong("operation_id"))
                .setVictimId(rs.getLong("victim_id"))
                .setAdminId(rs.getLong("admin_id"))
                .setStatus(rs.getString("status"))
                .setCreatedAt(Objects.nonNull(rs.getTimestamp("created_at"))
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null)
                .setUpdatedAt(Objects.nonNull(rs.getTimestamp("updated_at"))
                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                SearchOperationQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public SearchOperationResponse create(SearchOperationRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = searchOperationMapper.toParams(request, id);

        SearchOperation operation = namedParameterJdbcTemplate.query(
                SearchOperationQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return searchOperationMapper.toResponse(operation);
    }

    public Optional<SearchOperationResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_OPERATION_ID, id);
        try {
            SearchOperation operation = namedParameterJdbcTemplate.query(
                    SearchOperationQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(operation).map(searchOperationMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<SearchOperationResponse> getAll() {
        List<SearchOperation> operations = namedParameterJdbcTemplate.query(
                SearchOperationQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return operations.stream().map(searchOperationMapper::toResponse).toList();
    }

    public List<SearchOperationResponse> getByStatus(String status) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_STATUS, status);
        List<SearchOperation> operations = namedParameterJdbcTemplate.query(
                SearchOperationQueries.GET_BY_STATUS.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return operations.stream().map(searchOperationMapper::toResponse).toList();
    }

    public List<SearchOperationResponse> getByVictim(Long victimId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_VICTIM_ID, victimId);
        List<SearchOperation> operations = namedParameterJdbcTemplate.query(
                SearchOperationQueries.GET_BY_VICTIM.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return operations.stream().map(searchOperationMapper::toResponse).toList();
    }

    public List<SearchOperationResponse> getByAdmin(Long adminId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_ADMIN_ID, adminId);
        List<SearchOperation> operations = namedParameterJdbcTemplate.query(
                SearchOperationQueries.GET_BY_ADMIN.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return operations.stream().map(searchOperationMapper::toResponse).toList();
    }

    @Transactional
    public SearchOperationResponse update(Long id, SearchOperationRequest request) {
        MapSqlParameterSource params = searchOperationMapper.toParams(request, id);
        SearchOperation operation = namedParameterJdbcTemplate.query(
                SearchOperationQueries.UPDATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return searchOperationMapper.toResponse(operation);
    }

    @Transactional
    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_OPERATION_ID, id);
        namedParameterJdbcTemplate.update(
                SearchOperationQueries.DELETE.getSql(),
                params
        );
    }

    @Transactional
    public void updateStatus(Long operationId, String status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_OPERATION_ID, operationId)
                .addValue(PARAM_STATUS, status);
        namedParameterJdbcTemplate.update(
                SearchOperationQueries.UPDATE_OPERATION_STATUS.getSql(),
                params
        );
    }

    @Transactional
    public List<SearchOperationResponse> batchInsert(List<SearchOperationRequest> requests) {
        List<SearchOperationResponse> results = new java.util.ArrayList<>();
        for (SearchOperationRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }

    @Transactional
    public List<SearchOperationResponse> batchUpdate(List<SearchOperationRequest> requests, List<Long> ids) {
        if (requests.size() != ids.size()) {
            throw new IllegalArgumentException("Количество запросов должно совпадать с количеством ID");
        }

        List<SearchOperationResponse> results = new java.util.ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            results.add(update(ids.get(i), requests.get(i)));
        }
        return results;
    }
}
