package ru.mai.lighthouse.entity.api.victim;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о пропавшем человеке")
public class VictimResponse {

    @Schema(description = "ID пропавшего", example = "1")
    private Long victimId;

    @Schema(description = "Имя", example = "Петр")
    private String firstName;

    @Schema(description = "Фамилия", example = "Петров")
    private String lastName;

    @Schema(description = "Город", example = "Москва")
    private String city;

    @Schema(description = "Возраст", example = "25")
    private Integer age;

    @Schema(description = "Рост в см", example = "175")
    private Integer height;

    @Schema(description = "Ссылка на фото (S3)", example = "https://s3.example.com/photo.jpg")
    private String photo;

    @Schema(description = "Дополнительные заметки", example = "Последний раз видели в парке")
    private String notes;

    @Schema(description = "Дата создания записи")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;
}

