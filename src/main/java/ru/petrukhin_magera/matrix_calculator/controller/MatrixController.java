package ru.petrukhin_magera.matrix_calculator.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MatrixController {

    private final MatrixService matrixService;


    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping("/binary")
    public ResponseEntity<?> binaryCalculate(@RequestBody MatrixRequestBinary matrixRequestBinary, HttpSession httpSession) {
        String matrixOperation = matrixRequestBinary.getMatrixOperation();

        if (matrixOperation.equals("ADD") || matrixOperation.equals("SUB")
                || matrixOperation.equals("MUL")) {
            Matrix result = switch (matrixOperation) {
                case "ADD" -> matrixService.add(matrixRequestBinary.getMatrix1(),
                        matrixRequestBinary.getMatrix2());
                case "SUB" -> matrixService.sub(matrixRequestBinary.getMatrix1(),
                        matrixRequestBinary.getMatrix2());
                case "MUL" -> matrixService.mul(matrixRequestBinary.getMatrix1(),
                        matrixRequestBinary.getMatrix2());
                default -> throw new IllegalStateException("Unexpected value: " + matrixOperation);
            };

            addToHistory(httpSession, matrixOperation, matrixRequestBinary.getMatrix1(), matrixRequestBinary.getMatrix2(), result);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("/unary")
    public ResponseEntity<?> unaryCalculate(@RequestBody MatrixRequestUnary matrixRequestUnary, HttpSession httpSession) {
        String matrixOperation = matrixRequestUnary.getMatrixOperation();



        if (matrixOperation.equals("TRACE") || matrixOperation.equals("DET")) {
            Object result = switch (matrixOperation) {
                case "TRACE" -> matrixService.trace(matrixRequestUnary.getMatrix1());
                case "DET" -> matrixService.determinant(matrixRequestUnary.getMatrix1());
                default -> throw new IllegalStateException("Unexpected value: " + matrixOperation);
            };

            addToHistory(httpSession, matrixOperation, matrixRequestUnary.getMatrix1(), null, result);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryDto>> getHistory(HttpSession httpSession) {
        HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");

        if (historyService == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(List.of());
        }

        List<HistoryDto> history = historyService.getHistory();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(history); // Возвращаем тело нашего ответа
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory(HttpSession httpSession) {
        HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");

        // На всякий случай, хотя скорее всего использоваться не будет из-за аннотации @SessionScope
        if (historyService == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // Error 204
                    .build(); // Используем когда возвращаем пустое тело запроса
        }

        historyService.clearHistory();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    private void addToHistory(HttpSession httpSession, String operation, Matrix matrix1, Matrix matrix2, Object result) {

        HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");

        if (historyService == null) {
            historyService = new HistoryService();
            httpSession.setAttribute("historyService", historyService);
        }

        HistoryDto historyDto = new HistoryDto();
        historyDto.setOperation(operation);
        historyDto.setMatrix1(matrix1);
        historyDto.setMatrix2(matrix2);
        historyDto.setResult(result);

        historyService.add(historyDto);
    }
}
