package ru.mai.lighthouse.entity.api.center;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление волонтерского центра")
public class UpdateCenterRequest {

    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Название центра", example = "Центр поиска пропавших людей")
    private String title;

    @Size(max = 50, message = "Название города не должно превышать 50 символов")
    @Schema(description = "Город", example = "Москва")
    private String city;

    @Size(max = 6, message = "Почтовый индекс не должен превышать 6 символов")
    @Schema(description = "Почтовый индекс", example = "101000")
    private String postCode;

    @Size(max = 100, message = "Адрес не должен превышать 100 символов")
    @Schema(description = "Адрес", example = "ул. Ленина, д. 1")
    private String address;
}

