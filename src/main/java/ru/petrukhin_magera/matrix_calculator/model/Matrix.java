package ru.petrukhin_magera.matrix_calculator.model;

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

    @NotNull
    private int rows;

    @NotNull
    private int cols;

    @NotNull
    private double[][] data;

}
