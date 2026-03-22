package ru.petrukhin_magera.matrix_calculator.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/calculate")
@Validated
public class MatrixController {

    private final MatrixService matrixService;
    private final HistoryService historyService;

    public MatrixController(MatrixService matrixService, HistoryService historyService) {
        this.matrixService = matrixService;
        this.historyService = historyService;
    }

    @PostMapping("/binary")
    public ResponseEntity<?> binaryCalculate(
            @Valid @RequestBody MatrixRequestBinary matrixRequestBinary) {

        String matrixOperation = matrixRequestBinary.getMatrixOperation();
        Matrix matrix1 = matrixRequestBinary.getMatrix1();
        Matrix matrix2 = matrixRequestBinary.getMatrix2();

        log.info("Выполнение бинарной операции: {} над матрицами размером {}x{} и {}x{}",
                matrixOperation,
                matrix1.getRows(), matrix1.getCols(),
                matrix2.getRows(), matrix2.getCols());

        Object result;

        switch (matrixOperation) {
            case "ADD":
                result = matrixService.add(matrix1, matrix2);
                log.debug("Результат сложения матриц получен");
                break;
            case "SUB":
                result = matrixService.sub(matrix1, matrix2);
                log.debug("Результат вычитания матриц получен");
                break;
            case "MUL":
                result = matrixService.mul(matrix1, matrix2);
                log.debug("Результат умножения матриц получен");
                break;
            default:
                log.warn("Неизвестная операция: {}", matrixOperation);
                throw new IllegalArgumentException("Unknown operation: " + matrixOperation);
        }

        addToHistory(matrixOperation, matrix1, matrix2, result);
        log.info("Бинарная операция {} успешно выполнена и сохранена в историю", matrixOperation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping("/unary")
    public ResponseEntity<?> unaryCalculate(
            @Valid @RequestBody MatrixRequestUnary matrixRequestUnary) {

        String matrixOperation = matrixRequestUnary.getMatrixOperation();
        Matrix matrix1 = matrixRequestUnary.getMatrix1();

        log.info("Выполнение унарной операции: {} над матрицей размером {}x{}",
                matrixOperation, matrix1.getRows(), matrix1.getCols());

        Object result = switch (matrixOperation) {
            case "TRACE" -> {
                log.debug("Вычисление следа матрицы");
                yield matrixService.trace(matrix1);
            }
            case "DET" -> {
                log.debug("Вычисление определителя матрицы");
                yield matrixService.determinant(matrix1);
            }
            default -> {
                log.warn("Неизвестная операция: {}", matrixOperation);
                throw new IllegalArgumentException("Unknown operation: " + matrixOperation);
            }
        };

        addToHistory(matrixOperation, matrix1, null, result);
        log.info("Унарная операция {} успешно выполнена, результат: {}, сохранена в историю",
                matrixOperation, result);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryDto>> getHistory() {
        log.info("Запрос истории вычислений для текущей сессии");
        List<HistoryDto> history = historyService.getHistory();
        log.debug("Найдено записей в истории: {}", history.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(history);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        log.info("Очистка истории вычислений для текущей сессии");
        historyService.clearHistory();
        log.debug("История успешно очищена");
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
        log.debug("Операция {} добавлена в историю с ID: {}", operation, historyDto.getId());
    }
}