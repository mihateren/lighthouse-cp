package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SearchOperation {
    private Long operationId;
    private Long victimId;
    private Long adminId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

