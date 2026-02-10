package ru.petrukhin_magera.matrix_calculator.model.dto;


import ru.petrukhin_magera.matrix_calculator.model.Matrix;

public class MatrixRequest {


    private Matrix matrix1;
    private Matrix matrix2;
    private String matrixOperation;

    public MatrixRequest(Matrix matrix1, Matrix matrix2, String matrixOperation) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrixOperation = matrixOperation;
    }

    public MatrixRequest() {
    }

    public Matrix getMatrix1() {
        return matrix1;
    }

    public void setMatrix1(Matrix matrix1) {
        this.matrix1 = matrix1;
    }

    public Matrix getMatrix2() {
        return matrix2;
    }

    public void setMatrix2(Matrix matrix2) {
        this.matrix2 = matrix2;
    }

    public String getMatrixOperation() {
        return matrixOperation;
    }

    public void setMatrixOperation(String matrixOperation) {
        this.matrixOperation = matrixOperation;
    }
}
