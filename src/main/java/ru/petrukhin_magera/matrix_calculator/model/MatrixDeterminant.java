package ru.petrukhin_magera.matrix_calculator.model;

public class MatrixDeterminant {

    public static double determinant(double[][] matrix) {
        int n = matrix.length;

        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        double[][] subMatrix = new double[n-1][n-1];

        for (int k = 0; k < n; k++) {
            // Создание подматрицы
            for (int i = 1; i < n; i++) {
                int colIndex = 0;
                for (int j = 0; j < n; j++) {
                    if (j == k) continue;
                    subMatrix[i-1][colIndex] = matrix[i][j];
                    colIndex++;
                }
            }

            det += Math.pow(-1, k) * matrix[0][k] * determinant(subMatrix);
        }

        return det;
    }
}