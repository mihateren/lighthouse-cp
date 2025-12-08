package ru.mai.lighthouse.entity.message;

import lombok.Getter;

@Getter
public enum CommonMessage {

    RESULT_SUCCESS(0, "OK"),
    RESULT_ERROR(1, "Error"),
    ;

    private int code;
    private String message;

    private CommonMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
