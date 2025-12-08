package ru.mai.lighthouse.entity.api.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание записи в дневнике поиска")
public class SearchDiaryRequest {

    @NotNull(message = "ID команды обязателен")
    @Schema(description = "ID команды", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long teamId;

    @NotNull(message = "ID волонтера обязателен")
    @Schema(description = "ID волонтера", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long volunteerId;

    @NotBlank(message = "Тип активности обязателен")
    @Size(max = 30, message = "Тип активности не должен превышать 30 символов")
    @Schema(description = "Тип активности", example = "расклейка_листовок", 
            allowableValues = {"расклейка_листовок", "опрос_знакомых", "обход_территории", "работа_с_соцсетями", "другое"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String activityType;

    @NotBlank(message = "Описание обязательно")
    @Schema(description = "Описание активности", example = "Расклеил 50 листовок в районе парка", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Size(max = 100, message = "Местоположение не должно превышать 100 символов")
    @Schema(description = "Местоположение", example = "Парк Горького")
    private String location;
}

