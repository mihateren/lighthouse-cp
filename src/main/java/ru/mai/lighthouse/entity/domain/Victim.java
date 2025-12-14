package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Victim {
    private Long victimId;
    private String firstName;
    private String lastName;
    private String city;
    private Integer age;
    private Integer height;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

