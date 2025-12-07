package ru.mai.lighthouse.entity.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.mai.lighthouse.entity.message.CommonMessage;

@Data
@Accessors(chain = true)
public class Result<T> {

    @JsonProperty("payload")
    @Schema(description = "Данные")
    private T payload;

    @JsonProperty("result_code")
    @Schema(description = "Код ответа")
    private int resultCode;

    @JsonProperty("message")
    @Schema(description = "Сообщение", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    public static <T> Result<T> success(T payload) {
        return new Result<T>()
                .setResultCode(CommonMessage.RESULT_SUCCESS.getCode())
                .setMessage(CommonMessage.RESULT_SUCCESS.getMessage())
                .setPayload(payload);
    }
}
