package ru.petrukhin_magera.matrix_calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.petrukhin_magera.matrix_calculator.model.dto.HistoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@SessionScope
public class HistoryService {

    private final List<HistoryDto> history = new CopyOnWriteArrayList<>();
    private Long idCounter = 1L;

    public void add(HistoryDto historyDto) {
        historyDto.setId(idCounter++);
        history.addFirst(historyDto);
        log.debug("Добавлена запись в историю: ID={}, операция={}",
                historyDto.getId(), historyDto.getOperation());
    }

    public List<HistoryDto> getHistory() {
        log.debug("Возвращено записей истории: {}", history.size());
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        int sizeBefore = history.size();
        history.clear();
        idCounter = 1L;
        log.info("Очищена история: удалено {} записей", sizeBefore);
    }
}