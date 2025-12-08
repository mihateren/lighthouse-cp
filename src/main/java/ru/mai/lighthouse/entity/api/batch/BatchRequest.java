package ru.mai.lighthouse.entity.api.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос на пакетную операцию")
public class BatchRequest<T> {

    @NotEmpty(message = "Список элементов не может быть пустым")
    @Valid
    @Schema(description = "Список элементов для пакетной обработки", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> items;

    @Schema(description = "Останавливать ли обработку при первой ошибке", example = "false")
    private Boolean stopOnError = false;
}

