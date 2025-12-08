package ru.mai.lighthouse.entity.api.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание/обновление операции поиска")
public class SearchOperationRequest {

    @NotNull(message = "ID пропавшего обязателен")
    @Schema(description = "ID пропавшего человека", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long victimId;

    @NotNull(message = "ID админа обязателен")
    @Schema(description = "ID админа, создающего операцию", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long adminId;

    @Size(max = 20, message = "Статус не должен превышать 20 символов")
    @Schema(description = "Статус операции", example = "active", allowableValues = {"active", "closed", "found", "deceased"})
    private String status;
}

