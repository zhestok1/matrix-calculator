package ru.petrukhin_magera.matrix_calculator.model.entity;

import jakarta.persistence.*;

@Table(name = "history")
@Entity
public class MatrixEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matrix_operation")
    private String matrixOperation;

    @Column(name = "matrix_1")
    private String matrix1;

    @Column(name = "matrix_2")
    private String matrix2;

    @Column(name = "matrix_result")
    private String matrix_result;

    public MatrixEntity(Long id, String matrixOperation, String matrix1, String matrix2, String matrix_result) {
        this.id = id;
        this.matrixOperation = matrixOperation;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrix_result = matrix_result;
    }

    public MatrixEntity(String matrixOperation, String matrix1, String matrix2, String matrix_result) {
        this.matrixOperation = matrixOperation;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrix_result = matrix_result;
    }

    public MatrixEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatrixOperation() {
        return matrixOperation;
    }

    public void setMatrixOperation(String matrixOperation) {
        this.matrixOperation = matrixOperation;
    }

    public String getMatrix1() {
        return matrix1;
    }

    public void setMatrix1(String matrix1) {
        this.matrix1 = matrix1;
    }

    public String getMatrix2() {
        return matrix2;
    }

    public void setMatrix2(String matrix2) {
        this.matrix2 = matrix2;
    }

    public String getMatrix_result() {
        return matrix_result;
    }

    public void setMatrix_result(String matrix_result) {
        this.matrix_result = matrix_result;
    }
}
