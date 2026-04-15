package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;

/**
 * DTO для запроса бинарной операции над двумя матрицами.
 * <p>
 * Используется в эндпоинте POST /calculate/binary.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatrixRequestBinary {

    /**
     * Первая матрица для бинарной операции.
     */
    @NotNull(message = "Must be in binary operation")
    private Matrix matrix1;

    /**
     * Вторая матрица для бинарной операции.
     */
    @NotNull(message = "Must be in binary operation")
    private Matrix matrix2;

    /**
     * Тип бинарной операции.
     * <p>Допустимые значения: ADD, SUB, MUL.</p>
     */
    @NotEmpty(message = "Operation Type cannot be null")
    @Pattern(regexp = "ADD|SUB|MUL", message = "Operation must be ADD, SUB or MUL")
    private String matrixOperation;
}