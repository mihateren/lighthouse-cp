package ru.mai.lighthouse.entity.api.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание/обновление команды волонтеров")
public class VolunteerTeamRequest {

    @NotNull(message = "ID операции обязателен")
    @Schema(description = "ID операции поиска", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long operationId;

    @NotNull(message = "ID лидера команды обязателен")
    @Schema(description = "ID волонтера-лидера команды", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long teamLeaderId;

    @NotBlank(message = "Название команды обязательно")
    @Size(max = 50, message = "Название команды не должно превышать 50 символов")
    @Schema(description = "Название команды", example = "Команда Альфа", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;
}

