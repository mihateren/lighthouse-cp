package ru.mai.lighthouse.entity.api.volunteer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание/обновление волонтера")
public class VolunteerRequest {

    @NotNull(message = "ID центра обязателен")
    @Schema(description = "ID волонтерского центра", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long centerId;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 30, message = "Имя не должно превышать 30 символов")
    @Schema(description = "Имя", example = "Алексей", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 30, message = "Фамилия не должна превышать 30 символов")
    @Schema(description = "Фамилия", example = "Смирнов", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotNull(message = "Дата рождения обязательна")
    @Schema(description = "Дата рождения", example = "1995-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthdate;

    @NotBlank(message = "Телефон обязателен")
    @Size(max = 20, message = "Телефон не должен превышать 20 символов")
    @Schema(description = "Телефон", example = "+79991234567", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    @Schema(description = "Email", example = "volunteer@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Навыки и компетенции", example = "Опыт поисковых операций, знание местности")
    private String skills;

    @Schema(description = "Активен ли волонтер", example = "true")
    private Boolean isActive;
}

