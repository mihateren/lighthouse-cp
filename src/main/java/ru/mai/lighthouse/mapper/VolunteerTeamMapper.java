package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamRequest;
import ru.mai.lighthouse.entity.api.team.VolunteerTeamResponse;
import ru.mai.lighthouse.entity.domain.VolunteerTeam;

@Mapper(componentModel = "spring")
public interface VolunteerTeamMapper {

    VolunteerTeamResponse toResponse(VolunteerTeam team);

    default MapSqlParameterSource toParams(VolunteerTeamRequest request, Long teamId) {
        return new MapSqlParameterSource()
                .addValue("team_id", teamId)
                .addValue("operation_id", request.getOperationId())
                .addValue("team_leader_id", request.getTeamLeaderId())
                .addValue("team_name", request.getTeamName());
    }
}
