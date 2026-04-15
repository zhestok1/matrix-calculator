package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;

/**
 * DTO для хранения информации об операции в истории вычислений.
 * <p>
 * Содержит данные о выполненной операции, входных матрицах и результате.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    /**
     * Уникальный идентификатор записи в истории.
     */
    private Long id;

    /**
     * Тип выполненной операции (ADD, SUB, MUL, TRACE, DET, TRANSPOSE, INVERSE).
     */
    @NotEmpty(message = "Operation Type cannot be null")
    @Size(min = 2, max = 30, message = "Name of operation must be between 2 and 30 symbols!")
    private String operation;

    /**
     * Первая матрица (обязательная для всех операций).
     */
    @NotNull(message = "First matrix must be in all version!")
    private Matrix matrix1;

    /**
     * Вторая матрица (обязательна только для бинарных операций).
     */
    private Matrix matrix2;

    /**
     * Результат операции (может быть матрицей или числом).
     */
    @NotNull(message = "Result cannot be null")
    private Object result;
}