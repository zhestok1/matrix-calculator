package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatrixRequestBinary {

    @NotNull(message = "Must be in binary operation")
    private Matrix matrix1;

    @NotNull(message = "Must be in binary operation")
    private Matrix matrix2;

    @NotEmpty(message = "Operation Type cannot be null")
    @Pattern(regexp = "ADD|SUB|MUL", message = "Operation must be ADD, SUB or MUL")
    private String matrixOperation;

}
