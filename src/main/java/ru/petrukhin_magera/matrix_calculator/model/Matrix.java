package ru.petrukhin_magera.matrix_calculator.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель матрицы, используемая для хранения и передачи данных.
 * <p>
 * Содержит размерность матрицы (строки, столбцы) и сами данные в виде двумерного массива.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matrix {

    /**
     * Количество строк в матрице.
     * <p>Минимальное значение: 1.</p>
     */
    @Min(value = 1)
    private int rows;

    /**
     * Количество столбцов в матрице.
     * <p>Минимальное значение: 1.</p>
     */
    @Min(value = 1)
    private int cols;

    /**
     * Данные матрицы в виде двумерного массива.
     * <p>Не может быть null.</p>
     */
    @NotNull(message = "Matrix data cannot be null")
    private double[][] data;
}