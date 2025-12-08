package ru.mai.lighthouse.entity.api.status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на принятие решения админом по изменению статуса")
public class AdminDecisionRequest {

    @NotBlank(message = "Решение обязательно")
    @Size(max = 10, message = "Решение не должно превышать 10 символов")
    @Schema(description = "Решение админа", example = "approved", 
            allowableValues = {"approved", "rejected"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String decision;

    @Schema(description = "Заметки админа", example = "Статус подтвержден после проверки")
    private String adminNotes;
}

