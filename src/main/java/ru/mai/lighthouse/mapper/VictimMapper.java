package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.victim.VictimCreateRequest;
import ru.mai.lighthouse.entity.api.victim.VictimRequest;
import ru.mai.lighthouse.entity.api.victim.VictimResponse;
import ru.mai.lighthouse.entity.domain.Victim;

@Mapper(componentModel = "spring")
public interface VictimMapper {

    VictimRequest toRequest(VictimCreateRequest createRequest);

    VictimResponse toResponse(Victim victim);

    default MapSqlParameterSource toParams(VictimRequest request, Long victimId) {
        return new MapSqlParameterSource()
                .addValue("victim_id", victimId)
                .addValue("first_name", request.getFirstName())
                .addValue("last_name", request.getLastName())
                .addValue("city", request.getCity())
                .addValue("age", request.getAge())
                .addValue("height", request.getHeight())
                .addValue("notes", request.getNotes());
    }
}
