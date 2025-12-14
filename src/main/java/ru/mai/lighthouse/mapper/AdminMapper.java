package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.admin.AdminRequest;
import ru.mai.lighthouse.entity.api.admin.AdminResponse;
import ru.mai.lighthouse.entity.domain.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminResponse toResponse(Admin admin);

    default MapSqlParameterSource toParams(AdminRequest request, Long adminId) {
        return new MapSqlParameterSource()
                .addValue("admin_id", adminId)
                .addValue("center_id", request.getCenterId())
                .addValue("first_name", request.getFirstName())
                .addValue("last_name", request.getLastName())
                .addValue("email", request.getEmail())
                .addValue("birthdate", request.getBirthdate());
    }
}
