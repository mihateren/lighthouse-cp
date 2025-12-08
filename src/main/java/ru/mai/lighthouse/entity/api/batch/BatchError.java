package ru.mai.lighthouse.entity.api.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ошибка при пакетной обработке")
public class BatchError {

    @Schema(description = "Индекс элемента в исходном списке", example = "0")
    private Integer index;

    @Schema(description = "Описание ошибки", example = "Поле email обязательно")
    private String message;
}

