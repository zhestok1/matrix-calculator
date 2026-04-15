package ru.petrukhin_magera.matrix_calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.petrukhin_magera.matrix_calculator.model.dto.HistoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Сервис для управления историей матричных вычислений в рамках HTTP-сессии.
 * <p>
 * Обеспечивает хранение, добавление, получение и очистку истории операций,
 * выполненных пользователем за время одной сессии. История сохраняется в памяти
 * и привязана к конкретной сессии благодаря аннотации {@link SessionScope}.
 * </p>
 *
 * <p>Особенности реализации:</p>
 * <ul>
 *     <li>Использует {@link CopyOnWriteArrayList} для потокобезопасного хранения</li>
 *     <li>Новые записи добавляются в начало списка (последние операции сверху)</li>
 *     <li>Каждая запись получает уникальный автоматически инкрементируемый идентификатор</li>
 *     <li>При очистке истории сбрасывается счётчик идентификаторов</li>
 * </ul>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@Slf4j
@Service
@SessionScope
public class HistoryService {

    /**
     * Потокобезопасный список для хранения DTO истории вычислений.
     * <p>
     * {@link CopyOnWriteArrayList} выбран для обеспечения безопасности в многопоточной среде
     * при относительно небольшом количестве операций чтения/записи истории.
     * </p>
     */
    private final List<HistoryDto> history = new CopyOnWriteArrayList<>();

    /**
     * Счётчик для генерации уникальных идентификаторов записей истории.
     * <p>
     * Начинается с 1 и инкрементируется при каждом добавлении новой записи.
     * Сбрасывается в 1 при очистке истории.
     * </p>
     */
    private Long idCounter = 1L;

    /**
     * Добавляет новую запись в историю вычислений.
     * <p>
     * Автоматически присваивает записи уникальный идентификатор (текущее значение счётчика
     * с последующим инкрементом) и помещает её в начало списка, чтобы последние операции
     * отображались первыми.
     * </p>
     *
     * @param historyDto DTO с данными об операции (матрицы, результат, тип операции)
     *                   Не должен быть {@code null}. Поле id будет установлено автоматически.
     */
    public void add(HistoryDto historyDto) {
        historyDto.setId(idCounter++);
        history.addFirst(historyDto);
        log.debug("Добавлена запись в историю: ID={}, операция={}",
                historyDto.getId(), historyDto.getOperation());
    }

    /**
     * Возвращает копию текущего списка истории вычислений.
     * <p>
     * Возвращается новый {@link ArrayList}, содержащий все записи истории.
     * Это предотвращает прямое изменение внутреннего списка извне.
     * </p>
     *
     * @return новый список {@link List<HistoryDto>} со всеми записями истории.
     *         Если история пуста, возвращается пустой список (не {@code null}).
     */
    public List<HistoryDto> getHistory() {
        log.debug("Возвращено записей истории: {}", history.size());
        return new ArrayList<>(history);
    }

    /**
     * Полностью очищает историю вычислений для текущей сессии.
     * <p>
     * Удаляет все записи из списка истории и сбрасывает счётчик идентификаторов на 1.
     * После выполнения этого метода история для текущей сессии становится пустой,
     * а новые операции будут получать идентификаторы, начиная с 1.
     * </p>
     *
     * <p>Логирует количество удалённых записей на уровне INFO.</p>
     */
    public void clearHistory() {
        int sizeBefore = history.size();
        history.clear();
        idCounter = 1L;
        log.info("Очищена история: удалено {} записей", sizeBefore);
    }
}