package ru.mai.lighthouse.entity.api.center;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ при создании волонтерского центра")
public class CreateCenterResponse {

    @Schema(description = "ID созданного центра", example = "1")
    private Long centerId;
}
