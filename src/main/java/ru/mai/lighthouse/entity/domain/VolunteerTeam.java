package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class VolunteerTeam {
    private Long teamId;
    private Long operationId;
    private Long teamLeaderId;
    private String teamName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

