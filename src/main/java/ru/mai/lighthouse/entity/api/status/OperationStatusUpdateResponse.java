package ru.mai.lighthouse.entity.api.status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о предложении изменения статуса операции")
public class OperationStatusUpdateResponse {

    @Schema(description = "ID предложения", example = "1")
    private Long updateId;

    @Schema(description = "ID операции поиска", example = "1")
    private Long operationId;

    @Schema(description = "ID волонтера", example = "1")
    private Long volunteerId;

    @Schema(description = "Предлагаемый статус", example = "found")
    private String proposedStatus;

    @Schema(description = "Причина изменения статуса", example = "Человек найден живым")
    private String reason;

    @Schema(description = "Решение админа", example = "approved", allowableValues = {"pending", "approved", "rejected"})
    private String adminDecision;

    @Schema(description = "Заметки админа", example = "Статус подтвержден")
    private String adminNotes;

    @Schema(description = "Дата создания предложения")
    private LocalDateTime createdAt;

    @Schema(description = "Дата принятия решения")
    private LocalDateTime decidedAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;
}

