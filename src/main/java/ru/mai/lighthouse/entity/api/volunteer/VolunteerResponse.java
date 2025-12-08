package ru.mai.lighthouse.entity.api.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о волонтере")
public class VolunteerResponse {

    @Schema(description = "ID волонтера", example = "1")
    private Long volunteerId;

    @Schema(description = "ID волонтерского центра", example = "1")
    private Long centerId;

    @Schema(description = "Имя", example = "Алексей")
    private String firstName;

    @Schema(description = "Фамилия", example = "Смирнов")
    private String lastName;

    @Schema(description = "Дата рождения", example = "1995-05-15")
    private LocalDate birthdate;

    @Schema(description = "Телефон", example = "+79991234567")
    private String phone;

    @Schema(description = "Email", example = "volunteer@example.com")
    private String email;

    @Schema(description = "Навыки и компетенции", example = "Опыт поисковых операций")
    private String skills;

    @Schema(description = "Активен ли волонтер", example = "true")
    private Boolean isActive;

    @Schema(description = "Дата регистрации")
    private LocalDate registrationDate;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;
}

