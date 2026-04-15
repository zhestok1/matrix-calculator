package ru.petrukhin_magera.matrix_calculator.model;

import lombok.extern.slf4j.Slf4j;

/**
 * Утилитарный класс для вычисления обратной матрицы.
 * <p>
 * Реализует метод через присоединённую (союзную) матрицу:
 * A⁻¹ = adj(A) / det(A)
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Slf4j
public class MatrixInverse {

    /**
     * Вычисляет обратную матрицу для заданной квадратной матрицы.
     *
     * @param matrix квадратная невырожденная матрица
     * @return обратная матрица
     * @throws IllegalArgumentException если матрица вырождена (det = 0) или не квадратная
     */
    public static double[][] inverse(double[][] matrix) {
        int n = matrix.length;
        log.debug("Вычисление обратной матрицы размером {}x{}", n, n);

        // Проверка вырожденности
        double det = MatrixDeterminant.determinant(matrix);
        if (Math.abs(det) < 1e-10) {
            log.error("Матрица вырождена (det = {}), обратная матрица не существует", det);
            throw new IllegalArgumentException("Matrix is singular, inverse does not exist");
        }

        // Базовый случай: матрица 1x1
        if (n == 1) {
            double[][] result = {{1.0 / matrix[0][0]}};
            log.debug("Обратная матрица 1x1: {}", result[0][0]);
            return result;
        }

        // Вычисление обратной матрицы через присоединённую
        double[][] inverse = new double[n][n];
        double[][] adjoint = adjoint(matrix, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = adjoint[i][j] / det;
            }
        }

        log.debug("Обратная матрица {}x{} вычислена", n, n);
        return inverse;
    }

    /**
     * Вычисляет присоединённую (союзную) матрицу.
     * <p>
     * Присоединённая матрица — это транспонированная матрица алгебраических дополнений.
     * </p>
     *
     * @param matrix исходная матрица
     * @param n      размер матрицы
     * @return присоединённая матрица
     */
    private static double[][] adjoint(double[][] matrix, int n) {
        double[][] adj = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] minor = getMinor(matrix, i, j, n);
                double cofactor = Math.pow(-1, i + j) * MatrixDeterminant.determinant(minor);
                adj[j][i] = cofactor; // транспонирование при присвоении
            }
        }

        return adj;
    }

    /**
     * Возвращает минор матрицы — подматрицу, полученную удалением указанной строки и столбца.
     *
     * @param matrix исходная матрица
     * @param row    удаляемая строка
     * @param col    удаляемый столбец
     * @param n      размер исходной матрицы
     * @return матрица-минор размером (n-1)x(n-1)
     */
    private static double[][] getMinor(double[][] matrix, int row, int col, int n) {
        double[][] minor = new double[n - 1][n - 1];
        int minorRow = 0, minorCol;

        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            minorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }

        return minor;
    }
}