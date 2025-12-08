package ru.mai.lighthouse.entity.api.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain=true)
@Schema(description = "Ответ на пакетную операцию")
public class BatchResponse<T> {

    @Schema(description = "Количество успешно обработанных элементов")
    private Integer successCount;

    @Schema(description = "Количество элементов с ошибками")
    private Integer errorCount;

    @Schema(description = "Список успешно обработанных элементов")
    private List<T> successItems;

    @Schema(description = "Список ошибок (индекс элемента и описание ошибки)")
    private List<BatchError> errors;
}

