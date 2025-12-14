package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerRequest;
import ru.mai.lighthouse.entity.api.volunteer.VolunteerResponse;
import ru.mai.lighthouse.entity.domain.Volunteer;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface VolunteerMapper {

    VolunteerResponse toResponse(Volunteer volunteer);

    default MapSqlParameterSource toParams(VolunteerRequest request, Long volunteerId) {
        return new MapSqlParameterSource()
                .addValue("volunteer_id", volunteerId)
                .addValue("center_id", request.getCenterId())
                .addValue("first_name", request.getFirstName())
                .addValue("last_name", request.getLastName())
                .addValue("birthdate", request.getBirthdate())
                .addValue("phone", request.getPhone())
                .addValue("email", request.getEmail())
                .addValue("skills", request.getSkills())
                .addValue("is_active", Objects.requireNonNullElse(request.getIsActive(), true));
    }
}
