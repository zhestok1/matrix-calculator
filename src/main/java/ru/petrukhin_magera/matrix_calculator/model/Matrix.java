package ru.petrukhin_magera.matrix_calculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matrix {

    private int rows;

    private int cols;

    private double[][] data;

}
