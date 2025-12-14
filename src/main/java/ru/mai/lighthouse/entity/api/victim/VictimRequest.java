package ru.mai.lighthouse.entity.api.victim;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание/обновление данных о пропавшем человеке")
public class VictimRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(max = 30, message = "Имя не должно превышать 30 символов")
    @Schema(description = "Имя", example = "Петр", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 30, message = "Фамилия не должна превышать 30 символов")
    @Schema(description = "Фамилия", example = "Петров", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Город обязателен")
    @Size(max = 50, message = "Название города не должно превышать 50 символов")
    @Schema(description = "Город", example = "Москва", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 149, message = "Возраст должен быть меньше 150")
    @Schema(description = "Возраст", example = "25")
    private Integer age;

    @Min(value = 1, message = "Рост должен быть больше 0")
    @Max(value = 299, message = "Рост должен быть меньше 300")
    @Schema(description = "Рост в см", example = "175")
    private Integer height;

    @Schema(description = "Дополнительные заметки", example = "Последний раз видели в парке")
    private String notes;
}

