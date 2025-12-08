package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class Admin {
    private Long adminId;
    private Long centerId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
}

