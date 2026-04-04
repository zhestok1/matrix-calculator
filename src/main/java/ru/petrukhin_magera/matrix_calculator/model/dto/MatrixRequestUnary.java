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
@NoArgsConstructor
@AllArgsConstructor
public class MatrixRequestUnary {

    @NotNull(message = "Matrix must be in unary operation!")
    private Matrix matrix1;

    @NotEmpty(message = "Operation Type cannot be null")
    @Pattern(regexp = "TRACE|DET|TRANSPOSE|INVERSE", message = "Operation must be TRACE, DET, TRANSPOSE or INVERSE")
    private String matrixOperation;

}
