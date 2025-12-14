package ru.mai.lighthouse.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mai.lighthouse.entity.api.Result;
import ru.mai.lighthouse.exception.NotFoundException;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result<Void>> handleNotFoundException(NotFoundException e) {
        log.warn("Ресурс не найден: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Некорректный аргумент: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException e
    ) {
        String message = "Нарушение целостности данных";

        Throwable cause = e.getCause();
        if (cause instanceof SQLException sqlEx) {
            String sqlState = sqlEx.getSQLState();

            switch (sqlState) {
                case "23503" -> message = "Связанная сущность не найдена";
                case "23505" -> message = "Запись уже существует";
                case "23502" -> message = "Обязательное поле не заполнено";
                default -> message = "Ошибка целостности данных";
            }
        }

        log.warn("Ошибка БД (DataIntegrityViolation): {}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.error(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleGenericException(Exception e) {
        log.error("Неожиданная ошибка: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("Внутренняя ошибка сервера"));
    }
}

