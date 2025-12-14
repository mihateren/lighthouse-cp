package ru.mai.lighthouse.repository.operation_status_update;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateResponse;
import ru.mai.lighthouse.entity.domain.OperationStatusUpdate;
import ru.mai.lighthouse.mapper.OperationStatusUpdateMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OperationStatusUpdateRepository {

    // SQL параметры в snake_case
    private static final String PARAM_UPDATE_ID = "update_id";
    private static final String PARAM_OPERATION_ID = "operation_id";
    private static final String PARAM_VOLUNTEER_ID = "volunteer_id";
    private static final String PARAM_PROPOSED_STATUS = "proposed_status";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_ADMIN_DECISION = "admin_decision";
    private static final String PARAM_ADMIN_NOTES = "admin_notes";
    private static final String PARAM_STATUS = "status";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final OperationStatusUpdateMapper operationStatusUpdateMapper;

    private OperationStatusUpdate mapFromResultSet(ResultSet rs) throws SQLException {
        return new OperationStatusUpdate()
                .setUpdateId(rs.getLong("update_id"))
                .setOperationId(rs.getLong("operation_id"))
                .setVolunteerId(rs.getLong("volunteer_id"))
                .setProposedStatus(rs.getString("proposed_status"))
                .setReason(rs.getString("reason"))
                .setAdminDecision(rs.getString("admin_decision"))
                .setAdminNotes(rs.getString("admin_notes"))
                .setCreatedAt(Objects.nonNull(rs.getTimestamp("created_at"))
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null)
                .setDecidedAt(Objects.nonNull(rs.getTimestamp("decided_at"))
                    ? rs.getTimestamp("decided_at").toLocalDateTime()
                    : null)
                .setUpdatedAt(Objects.nonNull(rs.getTimestamp("updated_at"))
                    ? rs.getTimestamp("updated_at").toLocalDateTime()
                    : null);
    }

    public Long getNextId() {
        return namedParameterJdbcTemplate.queryForObject(
                OperationStatusUpdateQueries.GET_NEXT_ID.getSql(),
                new MapSqlParameterSource(),
                Long.class
        );
    }

    @Transactional
    public OperationStatusUpdateResponse create(OperationStatusUpdateRequest request) {
        Long id = getNextId();
        MapSqlParameterSource params = operationStatusUpdateMapper.toParams(request, id);

        OperationStatusUpdate update = namedParameterJdbcTemplate.query(
                OperationStatusUpdateQueries.CREATE.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );

        return operationStatusUpdateMapper.toResponse(update);
    }

    public Optional<OperationStatusUpdateResponse> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_UPDATE_ID, id);
        try {
            OperationStatusUpdate update = namedParameterJdbcTemplate.query(
                    OperationStatusUpdateQueries.GET_BY_ID.getSql(),
                    params,
                    rs -> {
                        if (rs.next()) {
                            return mapFromResultSet(rs);
                        }
                        return null;
                    }
            );
            return Optional.ofNullable(update).map(operationStatusUpdateMapper::toResponse);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OperationStatusUpdateResponse> getAll() {
        List<OperationStatusUpdate> updates = namedParameterJdbcTemplate.query(
                OperationStatusUpdateQueries.GET_ALL.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return updates.stream().map(operationStatusUpdateMapper::toResponse).toList();
    }

    public List<OperationStatusUpdateResponse> getByOperation(Long operationId) {
        MapSqlParameterSource params = new MapSqlParameterSource(PARAM_OPERATION_ID, operationId);
        List<OperationStatusUpdate> updates = namedParameterJdbcTemplate.query(
                OperationStatusUpdateQueries.GET_BY_OPERATION.getSql(),
                params,
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return updates.stream().map(operationStatusUpdateMapper::toResponse).toList();
    }

    public List<OperationStatusUpdateResponse> getPending() {
        List<OperationStatusUpdate> updates = namedParameterJdbcTemplate.query(
                OperationStatusUpdateQueries.GET_PENDING.getSql(),
                new MapSqlParameterSource(),
                (rs, rowNum) -> mapFromResultSet(rs)
        );
        return updates.stream().map(operationStatusUpdateMapper::toResponse).toList();
    }

    @Transactional
    public OperationStatusUpdateResponse updateDecision(Long id, String adminDecision, String adminNotes) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_UPDATE_ID, id)
                .addValue(PARAM_ADMIN_DECISION, adminDecision)
                .addValue(PARAM_ADMIN_NOTES, adminNotes);
        OperationStatusUpdate update = namedParameterJdbcTemplate.query(
                OperationStatusUpdateQueries.UPDATE_DECISION.getSql(),
                params,
                rs -> {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    }
                    return null;
                }
        );
        return operationStatusUpdateMapper.toResponse(update);
    }

    @Transactional
    public void updateOperationStatus(Long operationId, String status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_OPERATION_ID, operationId)
                .addValue(PARAM_STATUS, status);
        namedParameterJdbcTemplate.update(
                OperationStatusUpdateQueries.UPDATE_OPERATION_STATUS.getSql(),
                params
        );
    }

    @Transactional
    public List<OperationStatusUpdateResponse> batchInsert(List<OperationStatusUpdateRequest> requests) {
        List<OperationStatusUpdateResponse> results = new java.util.ArrayList<>();
        for (OperationStatusUpdateRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }
}
