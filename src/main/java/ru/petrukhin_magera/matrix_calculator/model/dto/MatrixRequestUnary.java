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
 * DTO для запроса унарной операции над одной матрицей.
 * <p>
 * Используется в эндпоинте POST /calculate/unary.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatrixRequestUnary {

    /**
     * Матрица для выполнения унарной операции.
     */
    @NotNull(message = "Matrix must be in unary operation!")
    private Matrix matrix1;

    /**
     * Тип унарной операции.
     * <p>Допустимые значения: TRACE, DET, TRANSPOSE, INVERSE.</p>
     */
    @NotEmpty(message = "Operation Type cannot be null")
    @Pattern(regexp = "TRACE|DET|TRANSPOSE|INVERSE", message = "Operation must be TRACE, DET, TRANSPOSE or INVERSE")
    private String matrixOperation;
}