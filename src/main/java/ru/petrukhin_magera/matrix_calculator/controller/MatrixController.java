package ru.petrukhin_magera.matrix_calculator.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;
import ru.petrukhin_magera.matrix_calculator.model.dto.HistoryDto;
import ru.petrukhin_magera.matrix_calculator.model.dto.MatrixRequestBinary;
import ru.petrukhin_magera.matrix_calculator.model.dto.MatrixRequestUnary;
import ru.petrukhin_magera.matrix_calculator.service.HistoryService;
import ru.petrukhin_magera.matrix_calculator.service.MatrixService;

import java.util.List;

@RestController
@RequestMapping("/calculate")
@Validated
public class MatrixController {

    private final MatrixService matrixService;
    private final HistoryService historyService; // Внедряем через конструктор

    public MatrixController(MatrixService matrixService, HistoryService historyService) {
        this.matrixService = matrixService;
        this.historyService = historyService; // Spring сам подставит сессионный бин
    }

    @PostMapping("/binary")
    public ResponseEntity<?> binaryCalculate(
            @Valid @RequestBody MatrixRequestBinary matrixRequestBinary) {

        String matrixOperation = matrixRequestBinary.getMatrixOperation();
        Matrix matrix1 = matrixRequestBinary.getMatrix1();
        Matrix matrix2 = matrixRequestBinary.getMatrix2();

        Object result;

        switch (matrixOperation) {
            case "ADD":
                result = matrixService.add(matrix1, matrix2);
                break;
            case "SUB":
                result = matrixService.sub(matrix1, matrix2);
                break;
            case "MUL":
                result = matrixService.mul(matrix1, matrix2);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + matrixOperation);
        }


        addToHistory(matrixOperation, matrix1, matrix2, result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping("/unary")
    public ResponseEntity<?> unaryCalculate(
            @Valid @RequestBody MatrixRequestUnary matrixRequestUnary) {

        String matrixOperation = matrixRequestUnary.getMatrixOperation();
        Matrix matrix1 = matrixRequestUnary.getMatrix1();

        Object result = switch (matrixOperation) {
            case "TRACE" -> matrixService.trace(matrix1);
            case "DET" -> matrixService.determinant(matrix1);
            default -> throw new IllegalArgumentException("Unknown operation: " + matrixOperation);
        };


        addToHistory(matrixOperation, matrix1, null, result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryDto>> getHistory() {
        // История автоматически привязана к текущей сессии благодаря @SessionScope
        List<HistoryDto> history = historyService.getHistory();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(history);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        historyService.clearHistory();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private void addToHistory(String operation, Matrix matrix1, Matrix matrix2, Object result) {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setOperation(operation);
        historyDto.setMatrix1(matrix1);
        historyDto.setMatrix2(matrix2);
        historyDto.setResult(result);
        historyService.add(historyDto);
    }
}