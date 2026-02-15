package ru.petrukhin_magera.matrix_calculator.model.dto;

import ru.petrukhin_magera.matrix_calculator.model.Matrix;

public class HistoryDto {
    private Long id;
    private String operation;
    private Matrix matrix1;
    private Matrix matrix2;
    private Object result;

    public HistoryDto(Long id, String operation, Matrix matrix1, Matrix matrix2, Object result) {
        this.id = id;
        this.operation = operation;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
    }

    public HistoryDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
