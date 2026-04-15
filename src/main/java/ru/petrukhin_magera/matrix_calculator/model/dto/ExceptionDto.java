package ru.petrukhin_magera.matrix_calculator.model.dto;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для передачи информации об ошибке клиенту.
 * <p>
 * Используется глобальным обработчиком исключений для формирования
 * единообразного ответа при возникновении ошибок.
 * </p>
 *
 * @param message       краткое описание типа ошибки
 * @param detailMessage детальное описание причины ошибки
 * @param errorTime     временная метка возникновения ошибки
 */
public record ExceptionDto(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {
}