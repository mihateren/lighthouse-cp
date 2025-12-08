package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OperationStatusUpdate {
    private Long updateId;
    private Long operationId;
    private Long volunteerId;
    private String proposedStatus;
    private String reason;
    private String adminDecision;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;
    private LocalDateTime updatedAt;
}

