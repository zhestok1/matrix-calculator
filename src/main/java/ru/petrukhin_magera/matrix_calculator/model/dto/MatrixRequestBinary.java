package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Matrix matrix1;

    @NotNull
    private Matrix matrix2;

    @NotEmpty
    private String matrixOperation;

}
