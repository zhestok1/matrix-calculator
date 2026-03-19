package ru.petrukhin_magera.matrix_calculator.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matrix {

    @Min(value = 1)
    private int rows;

    @Min(value = 1)
    private int cols;

    @NotNull(message = "Matrix data cannot be null")
    private double[][] data;

}
