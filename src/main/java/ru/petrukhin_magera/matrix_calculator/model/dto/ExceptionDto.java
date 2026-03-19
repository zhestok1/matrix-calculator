package ru.petrukhin_magera.matrix_calculator.model.dto;

import java.time.LocalDateTime;

public record ExceptionDto(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {

}
