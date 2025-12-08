package ru.mai.lighthouse.entity.api.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Информация об админе")
public class AdminResponse {

    @Schema(description = "ID админа", example = "1")
    private Long adminId;

    @Schema(description = "ID волонтерского центра", example = "1")
    private Long centerId;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Schema(description = "Email", example = "admin@example.com")
    private String email;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthdate;
}

