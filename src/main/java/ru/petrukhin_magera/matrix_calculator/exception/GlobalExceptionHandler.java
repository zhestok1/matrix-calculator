package ru.petrukhin_magera.matrix_calculator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.petrukhin_magera.matrix_calculator.model.dto.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // Логируем ошибку валидации
        logger.warn("Ошибка валидации: {}", message);

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

        // Логируем ошибку в данных
        logger.warn("Ошибка в данных: {}", ex.getMessage());

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

        // Логируем непредвиденную ошибку с полным стек-трейсом
        logger.error("Непредвиденная внутренняя ошибка сервера: ", ex);

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