package ru.petrukhin_magera.matrix_calculator.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;
import ru.petrukhin_magera.matrix_calculator.model.dto.HistoryDto;
import ru.petrukhin_magera.matrix_calculator.model.dto.MatrixRequest;
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

    @PostMapping
    public Matrix calculate(@RequestBody MatrixRequest matrixRequest, HttpSession httpSession) {
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

            HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");
            if (historyService == null) {
                historyService = new HistoryService();
                httpSession.setAttribute("historyService", historyService);
            }

            HistoryDto historyDto = new HistoryDto();

            historyDto.setOperation(matrixOperation);
            historyDto.setMatrix1(matrixRequest.getMatrix1());
            historyDto.setMatrix2(matrixRequest.getMatrix2());
            historyDto.setResult(result);

            historyService.add(historyDto);
            return result;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping("/history")
    public List<HistoryDto> getHistory(HttpSession httpSession) {
        HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");

        if (historyService == null) {
            return List.of();
        }

        return historyService.getHistory();
    }

    @DeleteMapping("/history")
    public void clearHistory(HttpSession httpSession) {
        HistoryService historyService = (HistoryService) httpSession.getAttribute("historyService");
        if (historyService != null) {
            historyService.clearHistory();
        }
    }
}
