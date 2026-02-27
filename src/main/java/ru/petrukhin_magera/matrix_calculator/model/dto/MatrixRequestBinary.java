package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
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

    private Matrix matrix1;

    private Matrix matrix2;

    @NotEmpty
    private String matrixOperation;

}
