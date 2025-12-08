package ru.mai.lighthouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryRequest;
import ru.mai.lighthouse.entity.api.diary.SearchDiaryResponse;
import ru.mai.lighthouse.entity.domain.SearchDiary;

@Mapper
public interface SearchDiaryMapper {
    SearchDiaryMapper INSTANCE = Mappers.getMapper(SearchDiaryMapper.class);

    SearchDiaryResponse toResponse(SearchDiary diary);

    default MapSqlParameterSource toParams(SearchDiaryRequest request, Long diaryId) {
        return new MapSqlParameterSource()
                .addValue("diary_id", diaryId)
                .addValue("team_id", request.getTeamId())
                .addValue("volunteer_id", request.getVolunteerId())
                .addValue("activity_type", request.getActivityType())
                .addValue("description", request.getDescription())
                .addValue("location", request.getLocation());
    }
}
