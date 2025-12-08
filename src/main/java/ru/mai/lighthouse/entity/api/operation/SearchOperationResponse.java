package ru.mai.lighthouse.entity.api.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Информация об операции поиска")
public class SearchOperationResponse {

    @Schema(description = "ID операции", example = "1")
    private Long operationId;

    @Schema(description = "ID пропавшего человека", example = "1")
    private Long victimId;

    @Schema(description = "ID админа", example = "1")
    private Long adminId;

    @Schema(description = "Статус операции", example = "active")
    private String status;

    @Schema(description = "Дата создания операции")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;
}

