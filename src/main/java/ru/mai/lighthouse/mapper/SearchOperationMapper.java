package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.operation.SearchOperationRequest;
import ru.mai.lighthouse.entity.api.operation.SearchOperationResponse;
import ru.mai.lighthouse.entity.domain.SearchOperation;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface SearchOperationMapper {

    SearchOperationResponse toResponse(SearchOperation operation);

    default MapSqlParameterSource toParams(SearchOperationRequest request, Long operationId) {
        return new MapSqlParameterSource()
                .addValue("operation_id", operationId)
                .addValue("victim_id", request.getVictimId())
                .addValue("admin_id", request.getAdminId())
                .addValue("status", Objects.requireNonNullElse(request.getStatus(), "active"));
    }
}
