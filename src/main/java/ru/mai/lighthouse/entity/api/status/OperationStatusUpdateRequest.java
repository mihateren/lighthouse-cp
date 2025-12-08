package ru.mai.lighthouse.entity.api.status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на предложение изменения статуса операции")
public class OperationStatusUpdateRequest {

    @NotNull(message = "ID операции обязателен")
    @Schema(description = "ID операции поиска", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long operationId;

    @NotNull(message = "ID волонтера обязателен")
    @Schema(description = "ID волонтера, предлагающего изменение", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long volunteerId;

    @NotBlank(message = "Предлагаемый статус обязателен")
    @Size(max = 20, message = "Статус не должен превышать 20 символов")
    @Schema(description = "Предлагаемый статус", example = "found", 
            allowableValues = {"found", "deceased", "closed"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String proposedStatus;

    @Schema(description = "Причина изменения статуса", example = "Человек найден живым")
    private String reason;
}

