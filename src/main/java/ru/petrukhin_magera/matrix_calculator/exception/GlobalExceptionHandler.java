package ru.petrukhin_magera.matrix_calculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.petrukhin_magera.matrix_calculator.model.dto.ExceptionDto;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.warn("Ошибка валидации запроса: {}", message);
        log.debug("Детали ошибки валидации: {}", ex.getBindingResult().getAllErrors());

        ExceptionDto exceptionDto = new ExceptionDto(
                "Ошибка валидации",
                message,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.warn("Ошибка в данных запроса: {}", ex.getMessage());

        ExceptionDto exceptionDto = new ExceptionDto(
                "Ошибка в данных",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGenericException(
            Exception ex) {

        log.error("Непредвиденная внутренняя ошибка сервера: ", ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "Внутренняя ошибка сервера",
                "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }
}