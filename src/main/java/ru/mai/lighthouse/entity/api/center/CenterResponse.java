package ru.mai.lighthouse.entity.api.center;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о волонтерском центре")
public class CenterResponse {

    @Schema(description = "ID центра", example = "1")
    private Long centerId;

    @Schema(description = "Название центра", example = "Центр поиска пропавших людей")
    private String title;

    @Schema(description = "Город", example = "Москва")
    private String city;

    @Schema(description = "Почтовый индекс", example = "101000")
    private String postCode;

    @Schema(description = "Адрес", example = "ул. Ленина, д. 1")
    private String address;
}

