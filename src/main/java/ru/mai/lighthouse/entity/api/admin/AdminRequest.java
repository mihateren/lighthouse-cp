package ru.mai.lighthouse.entity.api.admin;

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
@Schema(description = "Запрос на создание/обновление админа")
public class AdminRequest {

    @NotNull(message = "ID центра обязателен")
    @Schema(description = "ID волонтерского центра", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long centerId;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 30, message = "Имя не должно превышать 30 символов")
    @Schema(description = "Имя", example = "Иван", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 30, message = "Фамилия не должна превышать 30 символов")
    @Schema(description = "Фамилия", example = "Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    @Schema(description = "Email", example = "admin@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull(message = "Дата рождения обязательна")
    @Schema(description = "Дата рождения", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthdate;
}

