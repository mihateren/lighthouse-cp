package ru.mai.lighthouse.entity.api.center;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание волонтерского центра")
public class CreateCenterRequest {

    @NotBlank(message = "Название центра обязательно")
    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Название центра", example = "Центр поиска пропавших людей", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "Город обязателен")
    @Size(max = 50, message = "Название города не должно превышать 50 символов")
    @Schema(description = "Город", example = "Москва", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotBlank(message = "Почтовый индекс обязателен")
    @Size(max = 6, message = "Почтовый индекс не должен превышать 6 символов")
    @Schema(description = "Почтовый индекс", example = "101000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postCode;

    @NotBlank(message = "Адрес обязателен")
    @Size(max = 100, message = "Адрес не должен превышать 100 символов")
    @Schema(description = "Адрес", example = "ул. Ленина, д. 1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
}
