package ru.mai.lighthouse.entity.api.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Запись в дневнике поиска")
public class SearchDiaryResponse {

    @Schema(description = "ID записи", example = "1")
    private Long diaryId;

    @Schema(description = "ID команды", example = "1")
    private Long teamId;

    @Schema(description = "ID волонтера", example = "1")
    private Long volunteerId;

    @Schema(description = "Тип активности", example = "расклейка_листовок")
    private String activityType;

    @Schema(description = "Описание активности", example = "Расклеил 50 листовок в районе парка")
    private String description;

    @Schema(description = "Местоположение", example = "Парк Горького")
    private String location;

    @Schema(description = "Дата создания записи")
    private LocalDateTime createdAt;
}

