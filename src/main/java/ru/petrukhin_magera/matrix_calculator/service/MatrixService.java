package ru.petrukhin_magera.matrix_calculator.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;
import ru.petrukhin_magera.matrix_calculator.model.entity.MatrixEntity;
import ru.petrukhin_magera.matrix_calculator.repository.MatrixRepository;

@Service
public class MatrixService {

    final private MatrixRepository matrixRepository;

    public MatrixService(MatrixRepository matrixRepository) {
        this.matrixRepository = matrixRepository;
    }

    private String matrixToString(Matrix matrix) {
        StringBuilder sb = new StringBuilder();
        double[][] data = matrix.getData();

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                sb.append(data[i][j]);
                if (j < matrix.getCols() - 1) {
                    sb.append(",");
                }
            }
            if (i < matrix.getRows() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    // Преобразование строки в Matrix (опционально, если нужно будет читать из БД)
    public Matrix stringToMatrix(String matrixStr) {
        String[] rows = matrixStr.split(";");
        int rowCount = rows.length;
        int colCount = rows[0].split(",").length;

        double[][] data = new double[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < colCount; j++) {
                data[i][j] = Double.parseDouble(cols[j]);
            }
        }

        return new Matrix(rowCount, colCount, data);
    }

    @Transactional
    public Matrix add(Matrix matrix1, Matrix matrix2) {

        if (matrix1.getRows() == matrix2.getRows() && matrix1.getCols() == matrix2.getCols()) {

            double[][] newData = new double[matrix1.getRows()][matrix1.getCols()];
            double[][] m1 = matrix1.getData();
            double[][] m2 = matrix2.getData();

            for (int i = 0; i < matrix1.getRows(); i++) {
                for (int j  = 0; j < matrix1.getCols(); j++) {
                    newData[i][j] = m1[i][j] + m2[i][j];
                }
            }

            Matrix result = new Matrix(matrix1.getRows(), matrix1.getCols(), newData);

            saveOperation("ADD", matrix1, matrix2, result);

            return result;

        }
        else {
            throw new IllegalArgumentException();
        }

    }

    public Matrix sub(Matrix matrix1, Matrix matrix2) {
        return null;
    }

    public Matrix mul(Matrix matrix1, Matrix matrix2) {
        return null;
    }

    private void saveOperation(String operation, Matrix matrix1, Matrix matrix2, Matrix result) {
        MatrixEntity entity = new MatrixEntity(
                operation,
                matrixToString(matrix1),
                matrixToString(matrix2),
                matrixToString(result)
        );
        matrixRepository.save(entity);
    }
}
