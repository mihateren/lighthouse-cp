package ru.mai.lighthouse.entity.message;

import lombok.Getter;

@Getter
public enum CommonMessage {

    RESULT_SUCCESS(0, "OK"),
    ;

    private int code;
    private String message;

    private CommonMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
