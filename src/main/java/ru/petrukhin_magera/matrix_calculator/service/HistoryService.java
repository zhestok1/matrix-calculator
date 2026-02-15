package ru.petrukhin_magera.matrix_calculator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.petrukhin_magera.matrix_calculator.model.dto.HistoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
@SessionScope
public class HistoryService {

    private final List<HistoryDto> history = new CopyOnWriteArrayList<>();
    private Long idCounter = 1L;

    public void add(HistoryDto historyDto) {
        historyDto.setId(idCounter++);
        history.addFirst(historyDto);
    }

    public List<HistoryDto> getHistory() {
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        history.clear();
        idCounter = 1L;
    }
}
