package ru.petrukhin_magera.matrix_calculator.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatrixDeterminant {

    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        log.debug("Вычисление определителя матрицы размером {}x{}", n, n);

        if (n == 1) {
            log.debug("Определитель матрицы 1x1: {}", matrix[0][0]);
            return matrix[0][0];
        }
        if (n == 2) {
            double det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            log.debug("Определитель матрицы 2x2: {}", det);
            return det;
        }

        double det = 0;
        double[][] subMatrix = new double[n - 1][n - 1];

        for (int k = 0; k < n; k++) {
            for (int i = 1; i < n; i++) {
                int colIndex = 0;
                for (int j = 0; j < n; j++) {
                    if (j == k) continue;
                    subMatrix[i - 1][colIndex] = matrix[i][j];
                    colIndex++;
                }
            }
            double term = Math.pow(-1, k) * matrix[0][k] * determinant(subMatrix);
            det += term;
            log.trace("Рекурсивное вычисление: k={}, term={}, det={}", k, term, det);
        }

        log.debug("Определитель матрицы {}x{} вычислен: {}", n, n, det);
        return det;
    }
}