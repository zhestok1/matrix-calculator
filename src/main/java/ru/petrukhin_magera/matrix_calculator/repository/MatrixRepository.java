package ru.petrukhin_magera.matrix_calculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrukhin_magera.matrix_calculator.model.entity.MatrixEntity;

public interface MatrixRepository extends JpaRepository<MatrixEntity, Long> {

}
