package ru.mai.lighthouse.entity.api.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о команде волонтеров")
public class VolunteerTeamResponse {

    @Schema(description = "ID команды", example = "1")
    private Long teamId;

    @Schema(description = "ID операции поиска", example = "1")
    private Long operationId;

    @Schema(description = "ID лидера команды", example = "1")
    private Long teamLeaderId;

    @Schema(description = "Название команды", example = "Команда Альфа")
    private String teamName;

    @Schema(description = "Дата создания команды")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;
}

