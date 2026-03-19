package ru.petrukhin_magera.matrix_calculator.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.petrukhin_magera.matrix_calculator.model.Matrix;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {


    private Long id;

    @NotEmpty(message = "Operation Type cannot be null")
    @Size(min = 2, max = 30, message = "Name of operation must be between 2 and 30 symbols!")
    private String operation;

    @NotNull(message = "First matrix must be in all version!")
    private Matrix matrix1;

    private Matrix matrix2;

    @NotNull(message = "Result cannot be null")
    private Object result;
}
