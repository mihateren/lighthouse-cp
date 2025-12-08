package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Volunteer {
    private Long volunteerId;
    private Long centerId;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String phone;
    private String email;
    private String skills;
    private Boolean isActive;
    private LocalDate registrationDate;
    private LocalDateTime updatedAt;
}

