package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SearchDiary {
    private Long diaryId;
    private Long teamId;
    private Long volunteerId;
    private String activityType;
    private String description;
    private String location;
    private LocalDateTime createdAt;
}

