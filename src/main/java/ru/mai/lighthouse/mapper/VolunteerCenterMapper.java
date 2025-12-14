package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.center.CenterResponse;
import ru.mai.lighthouse.entity.api.center.CreateCenterRequest;
import ru.mai.lighthouse.entity.api.center.UpdateCenterRequest;
import ru.mai.lighthouse.entity.domain.VolunteerCenter;

@Mapper(componentModel = "spring")
public interface VolunteerCenterMapper {

    CenterResponse toResponse(VolunteerCenter center);

    default MapSqlParameterSource toParams(CreateCenterRequest request, Long centerId) {
        return new MapSqlParameterSource()
                .addValue("center_id", centerId)
                .addValue("title", request.getTitle())
                .addValue("city", request.getCity())
                .addValue("post_code", request.getPostCode())
                .addValue("address", request.getAddress());
    }

    default MapSqlParameterSource toParams(UpdateCenterRequest request, Long centerId) {
        return new MapSqlParameterSource()
                .addValue("center_id", centerId)
                .addValue("title", request.getTitle())
                .addValue("city", request.getCity())
                .addValue("post_code", request.getPostCode())
                .addValue("address", request.getAddress());
    }
}
