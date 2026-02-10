package ru.petrukhin_magera.matrix_calculator.model;

public class Matrix {

    private int rows;
    private int cols;
    private double[][] data;

    public Matrix(int rows, int cols, double[][] data) {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
    }

    public Matrix() {
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }
}
