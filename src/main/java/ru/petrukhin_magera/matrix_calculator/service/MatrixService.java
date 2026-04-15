package ru.petrukhin_magera.matrix_calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;
import ru.petrukhin_magera.matrix_calculator.model.MatrixDeterminant;
import ru.petrukhin_magera.matrix_calculator.model.MatrixInverse;

/**
 * Сервисный слой для выполнения матричных операций.
 * <p>
 * Реализует бизнес-логику сложения, вычитания, умножения матриц,
 * а также вычисления следа, определителя, транспонирования и обратной матрицы.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Slf4j
@Service
public class MatrixService {

    /**
     * Складывает две матрицы одинаковой размерности.
     *
     * @param matrix1 первая матрица
     * @param matrix2 вторая матрица
     * @return матрица-сумма
     * @throws IllegalArgumentException если размеры матриц не совпадают
     */
    public Matrix add(Matrix matrix1, Matrix matrix2) {
        log.debug("Проверка размеров матриц для сложения: {}x{} и {}x{}",
                matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());

        if (matrix1.getRows() == matrix2.getRows() && matrix1.getCols() == matrix2.getCols()) {

            double[][] newData = new double[matrix1.getRows()][matrix1.getCols()];
            double[][] m1 = matrix1.getData();
            double[][] m2 = matrix2.getData();

            for (int i = 0; i < matrix1.getRows(); i++) {
                for (int j = 0; j < matrix1.getCols(); j++) {
                    newData[i][j] = m1[i][j] + m2[i][j];
                }
            }

            log.debug("Сложение матриц выполнено успешно");
            return new Matrix(matrix1.getRows(), matrix1.getCols(), newData);

        } else {
            log.warn("Ошибка сложения: несовместимые размеры матриц {}x{} и {}x{}",
                    matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());
            throw new IllegalArgumentException("Size must have formal m×n and m×n!");
        }
    }

    /**
     * Вычитает вторую матрицу из первой (матрицы одинаковой размерности).
     *
     * @param matrix1 первая матрица (уменьшаемое)
     * @param matrix2 вторая матрица (вычитаемое)
     * @return матрица-разность
     * @throws IllegalArgumentException если размеры матриц не совпадают
     */
    public Matrix sub(Matrix matrix1, Matrix matrix2) {
        log.debug("Проверка размеров матриц для вычитания: {}x{} и {}x{}",
                matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());

        if (matrix1.getRows() == matrix2.getRows() && matrix1.getCols() == matrix2.getCols()) {

            double[][] newData = new double[matrix1.getRows()][matrix1.getCols()];
            double[][] m1 = matrix1.getData();
            double[][] m2 = matrix2.getData();

            for (int i = 0; i < matrix1.getRows(); i++) {
                for (int j = 0; j < matrix1.getCols(); j++) {
                    newData[i][j] = m1[i][j] - m2[i][j];
                }
            }

            log.debug("Вычитание матриц выполнено успешно");
            return new Matrix(matrix1.getRows(), matrix1.getCols(), newData);

        } else {
            log.warn("Ошибка вычитания: несовместимые размеры матриц {}x{} и {}x{}",
                    matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());
            throw new IllegalArgumentException("Size must have formal m×n and m×n!");
        }
    }

    /**
     * Умножает две матрицы.
     * <p>
     * Условие: количество столбцов первой матрицы равно количеству строк второй.
     * </p>
     *
     * @param matrix1 первая матрица (размером m×n)
     * @param matrix2 вторая матрица (размером n×k)
     * @Result матрица-произведение размером m×k
     * @throws IllegalArgumentException если размеры матриц не согласованы для умножения
     */
    public Matrix mul(Matrix matrix1, Matrix matrix2) {
        log.debug("Проверка размеров матриц для умножения: {}x{} и {}x{}",
                matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());

        if (matrix1.getCols() == matrix2.getRows()) {

            double[][] newData = new double[matrix1.getRows()][matrix2.getCols()];

            for (int i = 0; i < matrix1.getRows(); i++) {
                for (int j = 0; j < matrix2.getCols(); j++) {
                    double sum = 0;
                    for (int k = 0; k < matrix1.getCols(); k++) {
                        sum += matrix1.getData()[i][k] * matrix2.getData()[k][j];
                    }
                    newData[i][j] = sum;
                }
            }

            log.debug("Умножение матриц выполнено успешно, размер результата: {}x{}",
                    matrix1.getRows(), matrix2.getCols());
            return new Matrix(matrix1.getRows(), matrix2.getCols(), newData);

        } else {
            log.warn("Ошибка умножения: несовместимые размеры матриц {}x{} и {}x{}",
                    matrix1.getRows(), matrix1.getCols(), matrix2.getRows(), matrix2.getCols());
            throw new IllegalArgumentException("Size must have format n×m and m×k!");
        }
    }

    /**
     * Вычисляет след квадратной матрицы (сумму элементов главной диагонали).
     *
     * @param matrix1 квадратная матрица
     * @return значение следа
     * @throws IllegalArgumentException если матрица не квадратная
     */
    public double trace(Matrix matrix1) {
        log.debug("Проверка размера матрицы для вычисления следа: {}x{}",
                matrix1.getRows(), matrix1.getCols());

        if (matrix1.getCols() == matrix1.getRows()) {

            double[][] newData = matrix1.getData();
            double trace = 0;

            for (int i = 0; i < matrix1.getCols(); i++) {
                trace += newData[i][i];
            }

            log.debug("След матрицы вычислен успешно: {}", trace);
            return trace;

        } else {
            log.warn("Ошибка вычисления следа: матрица не квадратная ({}x{})",
                    matrix1.getRows(), matrix1.getCols());
            throw new IllegalArgumentException("Size must have formal n×n!");
        }
    }

    /**
     * Вычисляет определитель квадратной матрицы.
     *
     * @param matrix1 квадратная матрица
     * @return значение определителя
     * @throws IllegalArgumentException если матрица не квадратная
     */
    public double determinant(Matrix matrix1) {
        log.debug("Проверка размера матрицы для вычисления определителя: {}x{}",
                matrix1.getRows(), matrix1.getCols());

        if (matrix1.getCols() == matrix1.getRows()) {

            double[][] newData = matrix1.getData();
            double det = MatrixDeterminant.determinant(newData);

            log.debug("Определитель матрицы вычислен успешно: {}", det);
            return det;

        } else {
            log.warn("Ошибка вычисления определителя: матрица не квадратная ({}x{})",
                    matrix1.getRows(), matrix1.getCols());
            throw new IllegalArgumentException("Size must have format n×n!");
        }
    }

    /**
     * Транспонирует матрицу (меняет строки и столбцы местами).
     *
     * @param matrix1 исходная матрица размером m×n
     * @Result транспонированная матрица размером n×m
     */
    public Matrix transpose(Matrix matrix1) {
        log.debug("Транспонирование матрицы размером {}x{}", matrix1.getRows(), matrix1.getCols());

        double[][] original = matrix1.getData();
        double[][] transposed = new double[matrix1.getCols()][matrix1.getRows()];

        for (int i = 0; i < matrix1.getRows(); i++) {
            for (int j = 0; j < matrix1.getCols(); j++) {
                transposed[j][i] = original[i][j];
            }
        }

        log.debug("Транспонирование выполнено успешно, размер результата: {}x{}",
                matrix1.getCols(), matrix1.getRows());
        return new Matrix(matrix1.getCols(), matrix1.getRows(), transposed);
    }

    /**
     * Находит обратную матрицу для заданной квадратной невырожденной матрицы.
     *
     * @param matrix1 квадратная невырожденная матрица
     * @return обратная матрица
     * @throws IllegalArgumentException если матрица не квадратная или вырожденная
     */
    public Matrix inverse(Matrix matrix1) {
        log.debug("Проверка размера матрицы для нахождения обратной: {}x{}",
                matrix1.getRows(), matrix1.getCols());

        if (matrix1.getCols() != matrix1.getRows()) {
            log.warn("Ошибка: матрица не квадратная ({}x{})", matrix1.getRows(), matrix1.getCols());
            throw new IllegalArgumentException("Matrix must be square for inverse operation!");
        }

        double[][] data = matrix1.getData();
        double[][] inversedData = MatrixInverse.inverse(data);

        log.debug("Обратная матрица вычислена успешно");
        return new Matrix(matrix1.getRows(), matrix1.getCols(), inversedData);
    }
}