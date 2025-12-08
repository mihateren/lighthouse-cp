package ru.mai.lighthouse.entity.api.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на добавление волонтера в команду")
public class TeamMemberRequest {

    @NotNull(message = "ID волонтера обязателен")
    @Schema(description = "ID волонтера", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long volunteerId;
}

