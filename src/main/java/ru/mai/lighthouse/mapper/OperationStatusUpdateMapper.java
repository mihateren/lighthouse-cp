package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateRequest;
import ru.mai.lighthouse.entity.api.status.OperationStatusUpdateResponse;
import ru.mai.lighthouse.entity.domain.OperationStatusUpdate;

@Mapper(componentModel = "spring")
public interface OperationStatusUpdateMapper {

    OperationStatusUpdateResponse toResponse(OperationStatusUpdate update);

    default MapSqlParameterSource toParams(OperationStatusUpdateRequest request, Long updateId) {
        return new MapSqlParameterSource()
                .addValue("update_id", updateId)
                .addValue("operation_id", request.getOperationId())
                .addValue("volunteer_id", request.getVolunteerId())
                .addValue("proposed_status", request.getProposedStatus())
                .addValue("reason", request.getReason());
    }
}
