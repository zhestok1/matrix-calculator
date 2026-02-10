package ru.petrukhin_magera.matrix_calculator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;
import ru.petrukhin_magera.matrix_calculator.model.dto.MatrixRequest;
import ru.petrukhin_magera.matrix_calculator.service.MatrixService;

import java.util.Objects;

@RestController
@RequestMapping("/calculate")
public class MatrixController {

    private final MatrixService matrixService;

    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping
    public Matrix calculate(@RequestBody MatrixRequest matrixRequest) {
        String matrixOperation = matrixRequest.getMatrixOperation();

        Matrix result = null;

        if (matrixOperation.equals("ADD") || matrixOperation.equals("SUB") || matrixOperation.equals("MUL")) {
            result = switch (matrixOperation) {
                case "ADD" -> matrixService.add(matrixRequest.getMatrix1(),
                        matrixRequest.getMatrix2());
                case "SUB" -> matrixService.sub(matrixRequest.getMatrix1(),
                        matrixRequest.getMatrix2());
                case "MUL" -> matrixService.mul(matrixRequest.getMatrix1(),
                        matrixRequest.getMatrix2());
                default -> throw new IllegalStateException("Unexpected value: " + matrixOperation);
            };
            return result;
        }

        else {
            throw new IllegalArgumentException();
        }
    }

}
