package ru.petrukhin_magera.matrix_calculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.petrukhin_magera.matrix_calculator.model.dto.ExceptionDto;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для всего приложения.
 * <p>
 * Перехватывает различные типы исключений и возвращает клиенту
 * структурированный ответ с информацией об ошибке.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает ошибки валидации входных данных.
     * <p>
     * Возникает при использовании аннотаций {@link jakarta.validation.Valid}
     * и нарушении условий валидации.
     * </p>
     *
     * @param ex исключение валидации
     * @return ResponseEntity с DTO ошибки и статусом BAD_REQUEST (400)
     */
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

    /**
     * Обрабатывает исключения, связанные с некорректными аргументами.
     * <p>
     * Используется для ошибок, связанных с несовместимыми размерами матриц,
     * вырожденными матрицами и другими бизнес-ошибками.
     * </p>
     *
     * @param ex исключение IllegalArgumentException
     * @return ResponseEntity с DTO ошибки и статусом BAD_REQUEST (400)
     */
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

    /**
     * Обрабатывает все непредвиденные исключения.
     * <p>
     * Является "запасным" обработчиком для ошибок, не перехваченных более специфичными методами.
     * </p>
     *
     * @param ex исключение общего типа
     * @return ResponseEntity с общим сообщением об ошибке и статусом INTERNAL_SERVER_ERROR (500)
     */
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