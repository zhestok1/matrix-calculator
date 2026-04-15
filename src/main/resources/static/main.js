/**
 * =================================================================
 * МАТРИЧНЫЙ КАЛЬКУЛЯТОР - ОСНОВНАЯ ЛОГИКА
 * Версия: 2.1
 *
 * Описание: Полнофункциональный калькулятор для работы с матрицами
 * Поддерживает: сложение, вычитание, умножение, определитель,
 * след, транспонирование, обратную матрицу
 *
 * ИЗМЕНЕНИЯ В ВЕРСИИ 2.1:
 * - Исправлена работа с матрицей B для унарных операций
 * - Теперь можно использовать матрицу B для определителя, следа и т.д.
 * - Убрано принудительное отключение матрицы B
 *
 * КАК РАБОТАЕТ ПОДСКАЗКА:
 * - При выборе операции из выпадающего списка
 * - Справа посередине экрана появляется плашка
 * - Висит 5 секунд, затем исчезает
 * - Можно закрыть крестиком досрочно
 * =================================================================
 */

// ======================== КОНФИГУРАЦИЯ ========================

/**
 * СПИСОК ВСЕХ ДОСТУПНЫХ ОПЕРАЦИЙ
 */
const allOperations = [
    { name: "Сумма матриц", type: "double", apiName: "ADD", icon: "fa-plus", definition: "Сложение двух матриц одинаковой размерности. Каждый элемент результирующей матрицы равен сумме соответствующих элементов исходных матриц." },
    { name: "Вычитание матриц", type: "double", apiName: "SUB", icon: "fa-minus", definition: "Вычитание двух матриц одинаковой размерности. Каждый элемент результирующей матрицы равен разности соответствующих элементов исходных матриц." },
    { name: "Умножение матриц", type: "double", apiName: "MUL", icon: "fa-times", definition: "Умножение двух матриц. Количество столбцов первой матрицы должно равняться количеству строк второй. Результат — матрица, где элемент (i,j) равен сумме произведений элементов i-й строки первой матрицы на j-й столбец второй." },
    { name: "Определитель матрицы", type: "single", apiName: "DET", icon: "fa-calculator", definition: "Определитель (детерминант) квадратной матрицы — число, которое характеризует матрицу. Вычисляется только для квадратных матриц." },
    { name: "След матрицы", type: "single", apiName: "TRACE", icon: "fa-chart-line", definition: "След матрицы — сумма элементов главной диагонали квадратной матрицы." },
    { name: "Транспонирование матрицы", type: "single", apiName: "TRANSPOSE", icon: "fa-arrows-rotate", definition: "Транспонирование — операция, при которой строки матрицы становятся столбцами, а столбцы — строками. Размер матрицы меняется с m×n на n×m." },
    { name: "Обратная матрица", type: "single", apiName: "INVERSE", icon: "fa-divide", definition: "Обратная матрица — такая матрица, умножение которой на исходную дает единичную матрицу. Существует только для квадратных невырожденных матриц (определитель ≠ 0)." }
];

let isLoading = false;          // Флаг для предотвращения множественных запросов
let currentHistory = [];       // Хранилище истории вычислений
let tooltipTimeout = null;     // Таймер для автоматического закрытия подсказки

// ======================== ПЛАВАЮЩАЯ ПОДСКАЗКА СПРАВА ========================

/**
 * ПОКАЗ ПЛАВАЮЩЕЙ ПОДСКАЗКИ СПРАВА
 * Вызывается при выборе операции из выпадающего списка
 * Подсказка появляется справа посередине экрана и висит 5 секунд
 *
 * @param {string} operationApiName - API имя операции (ADD, SUB, DET и т.д.)
 */
function showFloatingTooltip(operationApiName) {
    // Находим операцию по ее apiName
    const operation = allOperations.find(op => op.apiName === operationApiName);

    // Если операция не найдена или нет определения - выходим
    if (!operation || !operation.definition) return;

    // Удаляем старую подсказку, если она есть (чтобы не было нагромождения)
    const existingTooltip = document.getElementById('floating-tooltip');
    if (existingTooltip) {
        existingTooltip.remove();
    }

    // Отменяем предыдущий таймер, если он был
    if (tooltipTimeout) {
        clearTimeout(tooltipTimeout);
    }

    // Создаем новую подсказку
    const tooltip = document.createElement('div');
    tooltip.id = 'floating-tooltip';
    tooltip.className = 'floating-tooltip';
    tooltip.innerHTML = `
        <button class="close-tip" onclick="closeFloatingTooltip()">&times;</button>
        <h4>
            <i class="fas ${operation.icon}"></i>
            ${operation.name}
        </h4>
        <p>${operation.definition}</p>
    `;

    // Добавляем на страницу
    document.body.appendChild(tooltip);

    // Устанавливаем таймер на автоматическое закрытие через 5 секунд
    tooltipTimeout = setTimeout(() => {
        closeFloatingTooltip();
    }, 5000);
}

/**
 * ЗАКРЫТЬ ПЛАВАЮЩУЮ ПОДСКАЗКУ
 */
function closeFloatingTooltip() {
    const tooltip = document.getElementById('floating-tooltip');
    if (tooltip) {
        tooltip.remove();
    }
    if (tooltipTimeout) {
        clearTimeout(tooltipTimeout);
        tooltipTimeout = null;
    }
}

// ======================== СТРАНИЦА "О НАС" ========================

/**
 * ПОКАЗ СТРАНИЦЫ "О НАС"
 *
 * - member-name: Имя и фамилия
 * - member-role: Должность/роль
 * - member-bio: Краткая информация о себе
 */
function showAboutPage() {
    const mainContainer = document.getElementById('main-container');
    const resultContainer = document.getElementById('result-container');
    const historyContainer = document.getElementById('history-container');
    const aboutContainer = document.getElementById('about-page-container');
    const footer = document.getElementById('footer');

    // Скрываем основные блоки и футер
    mainContainer.style.display = 'none';
    resultContainer.style.display = 'none';
    historyContainer.style.display = 'none';
    if (footer) footer.style.display = 'none';

    // Заполняем контейнер "О нас"
    aboutContainer.innerHTML = `
        <div class="about-page">
            <div class="about-header">
                <h1><i class="fas fa-users"></i> О нас</h1>
                <p>Команда разработчиков матричного калькулятора</p>
            </div>
            <div class="about-content">

                <!-- ===================================================== -->
                <!-- ПЕРВЫЙ УЧАСТНИК - Магера Никита (image_1.jpg) -->
                <!-- ===================================================== -->
                <div class="team-member">
                    <div class="member-photo">
                        <img src="image_1.jpg"
                             alt="Фото Магера Никита"
                             onerror="this.onerror=null; this.parentElement.innerHTML='<i class=\'fas fa-user-circle\'></i>';">
                    </div>
                    <div class="member-name">Магера Никита Алексеевич </div>
                    <div class="member-name">тг: @pow_diath</div>
                    <div class="member-role">Designer, Frontend Developer</div>
                </div>

                <!-- ===================================================== -->
                <!-- ВТОРОЙ УЧАСТНИК - Петрухин Роман (image_2.png) -->
                <!-- ===================================================== -->
                <div class="team-member">
                    <div class="member-photo">
                        <img src="image_2.jpg"
                             alt="Фото Петрухин Роман"
                             onerror="this.onerror=null; this.parentElement.innerHTML='<i class=\'fas fa-user-circle\'></i>';">
                    </div>
                    <div class="member-name">Петрухин Роман Андреевич</div>
                    <div class="member-name">тг: @aniwave13</div>
                    <div class="member-role">Team Lead, Backend Developer</div>
                </div>
            </div>
            <button class="back-to-main" onclick="hideAboutPage()">
                <i class="fas fa-arrow-left"></i> Вернуться к калькулятору
            </button>
        </div>
    `;
    aboutContainer.style.display = 'block';
}

/**
 * СКРЫТЬ СТРАНИЦУ "О НАС" И ВЕРНУТЬСЯ К КАЛЬКУЛЯТОРУ
 */
function hideAboutPage() {
    const mainContainer = document.getElementById('main-container');
    const resultContainer = document.getElementById('result-container');
    const historyContainer = document.getElementById('history-container');
    const aboutContainer = document.getElementById('about-page-container');
    const footer = document.getElementById('footer');

    // Показываем обратно основные блоки и футер
    mainContainer.style.display = 'flex';
    resultContainer.style.display = 'block';
    historyContainer.style.display = 'block';
    aboutContainer.style.display = 'none';
    if (footer) footer.style.display = 'block';

    aboutContainer.innerHTML = '';
}

// ======================== ОСНОВНЫЕ ФУНКЦИИ КАЛЬКУЛЯТОРА ========================

/**
 * СБРОС ПРИЛОЖЕНИЯ (обновление страницы + очистка истории на сервере)
 */
async function resetApp() {
    if (confirm('Вы уверены, что хотите обновить страницу? История будет очищена.')) {
        try {
            await fetch('http://localhost:8080/calculate/history', { method: 'DELETE' });
        } catch (error) {
            console.error('Ошибка очистки истории:', error);
        }
        location.reload();
    }
}

/**
 * ОТРИСОВКА МАТРИЦЫ (создание input полей)
 * @param {number} num - Номер матрицы (1 или 2)
 */
function renderMatrix(num) {
    const rows = parseInt(document.getElementById(`rows${num}`).value);
    const cols = parseInt(document.getElementById(`cols${num}`).value);
    const container = document.getElementById(`matrix${num}`);

    if (!container) return;

    container.innerHTML = '';
    container.style.gridTemplateColumns = `repeat(${cols}, minmax(70px, auto))`;
    container.style.gap = "8px";

    for (let i = 0; i < rows * cols; i++) {
        const input = document.createElement('input');
        input.type = 'number';
        input.className = 'matrix-cell';
        input.value = '0';
        input.step = "any";
        container.appendChild(input);
    }
}

/**
 * СБОР ДАННЫХ ИЗ МАТРИЦЫ
 * @param {number} num - Номер матрицы (1 или 2)
 * @returns {Object} Объект с rows, cols и data (двумерный массив)
 */
function getMatrixData(num) {
    const rows = parseInt(document.getElementById(`rows${num}`).value);
    const cols = parseInt(document.getElementById(`cols${num}`).value);
    const inputs = document.querySelectorAll(`#matrix${num} .matrix-cell`);

    const data = [];
    for (let i = 0; i < rows; i++) {
        data[i] = [];
        for (let j = 0; j < cols; j++) {
            const value = parseFloat(inputs[i * cols + j].value);
            data[i][j] = isNaN(value) ? 0 : value;
        }
    }

    return { rows, cols, data };
}

/**
 * ЗАПОЛНЕНИЕ МАТРИЦЫ ДАННЫМИ (для восстановления из истории)
 * @param {number} num - Номер матрицы (1 или 2)
 * @param {Object} matrixData - Данные матрицы в формате {rows, cols, data}
 */
function fillMatrixData(num, matrixData) {
    if (!matrixData || !matrixData.data) return;

    const rows = matrixData.rows;
    const cols = matrixData.cols;
    const inputs = document.querySelectorAll(`#matrix${num} .matrix-cell`);

    for (let i = 0; i < rows && i < matrixData.data.length; i++) {
        for (let j = 0; j < cols && j < matrixData.data[i].length; j++) {
            const index = i * cols + j;
            if (inputs[index]) {
                inputs[index].value = matrixData.data[i][j];
            }
        }
    }
}

/**
 * ОЧИСТКА МАТРИЦ (заполняет все поля нулями)
 */
function clearMatrices() {
    [1, 2].forEach(num => {
        const inputs = document.querySelectorAll(`#matrix${num} .matrix-cell`);
        inputs.forEach(input => input.value = '0');
    });
    showToast('Матрицы очищены', 'success');
}

/**
 * ОБРАБОТЧИК ИЗМЕНЕНИЯ ВЫБРАННОЙ ОПЕРАЦИИ
 * Показывает плавающую подсказку справа и обновляет доступные операции
 *
 * ИЗМЕНЕНИЯ В ВЕРСИИ 2.1:
 * - Убрано принудительное отключение матрицы B для унарных операций
 * - Теперь пользователь может сам выбирать, с какой матрицей работать
 */
function onOperationChange() {
    const operation = document.getElementById('choosing-operation').value;
    const checkA = document.getElementById('checkA').checked;
    const checkB = document.getElementById('checkB').checked;

    // ========== ПОКАЗЫВАЕМ ПЛАВАЮЩУЮ ПОДСКАЗКУ СПРАВА ==========
    if (operation) {
        showFloatingTooltip(operation);
    }

    // Определяем типы операций
    const isUnary = ['DET', 'TRACE', 'TRANSPOSE', 'INVERSE'].includes(operation);
    const isBinary = ['ADD', 'SUB', 'MUL'].includes(operation);
    const activeMatricesCount = (checkA ? 1 : 0) + (checkB ? 1 : 0);

    // ИЗМЕНЕНО: Для унарных операций больше НЕ отключаем принудительно матрицу B
    // Пользователь может сам выбрать, с какой матрицей работать

    // Для бинарных операций проверяем, что активны обе матрицы
    if (isBinary && activeMatricesCount !== 2) {
        document.getElementById('checkA').checked = true;
        document.getElementById('checkB').checked = true;
        updateUI();
        showToast('Для бинарной операции активны обе матрицы', 'info');
    }

    // Если активна только одна матрица для унарной операции - ничего не делаем
    // Пользователь сам выбрал, с какой матрицей работать
    if (isUnary && activeMatricesCount === 1) {
        const activeMatrix = checkA ? 'A' : 'B';
        console.log(`Унарная операция будет выполнена с матрицей ${activeMatrix}`);
    }

    // Если ни одна матрица не активна для унарной операции - активируем A по умолчанию
    if (isUnary && activeMatricesCount === 0) {
        document.getElementById('checkA').checked = true;
        updateUI();
        showToast('Активирована матрица A для выполнения операции', 'info');
    }
}

/**
 * УПРАВЛЕНИЕ ИНТЕРФЕЙСОМ (показ/скрытие матриц, обновление списка операций)
 */
function updateUI() {
    const checkA = document.getElementById('checkA').checked;
    const checkB = document.getElementById('checkB').checked;
    const select = document.getElementById('choosing-operation');
    const wrapper1 = document.getElementById('wrapper1');
    const wrapper2 = document.getElementById('wrapper2');
    const exchangeBtn = document.getElementById('matrix-exchange');
    const clearBtn = document.getElementById('clear-matrices');

    // Показываем/скрываем матрицы в зависимости от чекбоксов
    if (wrapper1) wrapper1.classList.toggle('hidden', !checkA);
    if (wrapper2) wrapper2.classList.toggle('hidden', !checkB);

    // Показываем/скрываем кнопки обмена и очистки
    if (exchangeBtn) exchangeBtn.style.display = (checkA && checkB) ? 'flex' : 'none';
    if (clearBtn) clearBtn.style.display = (checkA || checkB) ? 'flex' : 'none';

    const currentSelected = select.value;
    select.innerHTML = '<option value="" selected disabled>-- Выберите операцию --</option>';

    const activeMatricesCount = (checkA ? 1 : 0) + (checkB ? 1 : 0);

    // Заполняем список доступных операций в зависимости от количества активных матриц
    allOperations.forEach(op => {
        let shouldShow = false;

        if (activeMatricesCount === 2) {
            shouldShow = (op.type === "double");
        } else if (activeMatricesCount === 1) {
            shouldShow = (op.type === "single");
        }

        if (shouldShow) {
            const opt = document.createElement('option');
            opt.value = op.apiName;
            opt.textContent = op.name;
            select.appendChild(opt);
        }
    });

    // Восстанавливаем выбранное значение, если оно еще доступно
    if (currentSelected && [...select.options].some(opt => opt.value === currentSelected)) {
        select.value = currentSelected;
    }
}

/**
 * ОБМЕН МАТРИЦ МЕСТАМИ
 */
function swapMatrices() {
    const r1 = document.getElementById('rows1');
    const c1 = document.getElementById('cols1');
    const r2 = document.getElementById('rows2');
    const c2 = document.getElementById('cols2');

    const matrix1Data = getMatrixData(1);
    const matrix2Data = getMatrixData(2);

    const tempR = r1.value;
    const tempC = c1.value;

    r1.value = r2.value;
    c1.value = c2.value;
    r2.value = tempR;
    c2.value = tempC;

    renderMatrix(1);
    renderMatrix(2);

    fillMatrixData(1, matrix2Data);
    fillMatrixData(2, matrix1Data);

    showToast('Матрицы обменяны', 'success');
}

/**
 * ФОРМАТИРОВАНИЕ МАТРИЦЫ В HTML ТАБЛИЦУ (для отображения в истории)
 * @param {Object} matrixData - Данные матрицы
 * @returns {string} HTML строка с таблицей
 */
function formatMatrixAsTable(matrixData) {
    if (!matrixData || !matrixData.data || matrixData.data.length === 0) {
        return '<div>Нет данных</div>';
    }

    let html = '<table class="history-matrix-table">';
    for (let i = 0; i < matrixData.data.length; i++) {
        html += '<tr>';
        for (let j = 0; j < matrixData.data[i].length; j++) {
            let value = matrixData.data[i][j];
            let displayValue = typeof value === 'number' ? value.toFixed(4) : value;
            html += `<td>${displayValue}</td>`;
        }
        html += '</tr>';
    }
    html += '</table>';
    return html;
}

/**
 * ПОКАЗ РЕЗУЛЬТАТА ВЫЧИСЛЕНИЙ
 * @param {Object|number} result - Результат (матрица или число)
 * @param {string} operation - Название операции
 */
function showResult(result, operation) {
    const resultCard = document.getElementById('result-card');
    const resultContent = document.getElementById('result-content');

    if (!resultContent) return;

    let formattedHtml = '';

    // Если результат - матрица (объект с полями data, rows, cols)
    if (typeof result === 'object' && result.data) {
        formattedHtml = '<div style="overflow-x: auto;">';
        formattedHtml += '<table class="result-matrix-table">';
        for (let i = 0; i < result.data.length; i++) {
            formattedHtml += '<tr>';
            for (let j = 0; j < result.data[i].length; j++) {
                const val = typeof result.data[i][j] === 'number' ? result.data[i][j].toFixed(4) : result.data[i][j];
                formattedHtml += `<td>${val}</td>`;
            }
            formattedHtml += '</tr>';
        }
        formattedHtml += '</table></div>';
        formattedHtml += `<p style="margin-top: 15px; color: #666;">Размер: ${result.rows}×${result.cols}</p>`;
    }
    // Если результат - двумерный массив (для обратной совместимости)
    else if (Array.isArray(result) && result.length > 0 && Array.isArray(result[0])) {
        formattedHtml = '<div style="overflow-x: auto;">';
        formattedHtml += '<table class="result-matrix-table">';
        for (let i = 0; i < result.length; i++) {
            formattedHtml += '<tr>';
            for (let j = 0; j < result[i].length; j++) {
                const val = typeof result[i][j] === 'number' ? result[i][j].toFixed(4) : result[i][j];
                formattedHtml += `<td>${val}</td>`;
            }
            formattedHtml += '</tr>';
        }
        formattedHtml += '</table></div>';
        formattedHtml += `<p style="margin-top: 15px; color: #666;">Размер: ${result.length}×${result[0].length}</p>`;
    }
    else {
        // Результат - число (определитель, след)
        formattedHtml = `<div class="result-value">${typeof result === 'number' ? result.toFixed(6) : result}</div>`;
    }

    resultContent.innerHTML = formattedHtml;
    resultCard.style.display = 'block';
    resultCard.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

/**
 * ЗАКРЫТЬ КАРТОЧКУ РЕЗУЛЬТАТА
 */
function closeResult() {
    document.getElementById('result-card').style.display = 'none';
}

/**
 * ПОКАЗ УВЕДОМЛЕНИЯ (TOAST)
 * @param {string} message - Текст уведомления
 * @param {string} type - Тип: 'success', 'error', 'info'
 */
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i> ${message}`;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Добавляем стиль для анимации исчезновения уведомлений
const toastStyle = document.createElement('style');
toastStyle.textContent = `
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(toastStyle);

/**
 * ЗАГРУЗКА ИСТОРИИ С СЕРВЕРА
 */
async function refreshHistory() {
    try {
        const response = await fetch('http://localhost:8080/calculate/history');
        if (!response.ok) throw new Error('Ошибка загрузки');

        currentHistory = await response.json();
        const historyList = document.getElementById('history-list');

        if (currentHistory.length === 0) {
            historyList.innerHTML = `
                <div class="empty-history">
                    <i class="fas fa-inbox"></i>
                    <p>История пуста. Выполните вычисления, чтобы они появились здесь.</p>
                </div>
            `;
            return;
        }

        historyList.innerHTML = currentHistory.map((item, index) => {
            let resultDisplay = '';

            if (item.result && typeof item.result === 'object' && item.result.data) {
                resultDisplay = `
                    <div class="history-matrix-wrapper">
                        <strong>Результат (${item.result.rows}×${item.result.cols}):</strong>
                        <div style="margin-top: 8px;">
                            ${formatMatrixAsTable(item.result)}
                        </div>
                    </div>
                `;
            } else if (item.result && Array.isArray(item.result) && item.result.length > 0) {
                // Для обратной совместимости с массивом
                const matrixObj = { data: item.result, rows: item.result.length, cols: item.result[0].length };
                resultDisplay = `
                    <div class="history-matrix-wrapper">
                        <strong>Результат (${matrixObj.rows}×${matrixObj.cols}):</strong>
                        <div style="margin-top: 8px;">
                            ${formatMatrixAsTable(matrixObj)}
                        </div>
                    </div>
                `;
            } else {
                resultDisplay = `
                    <div class="history-number-result">
                        <strong>Результат:</strong>
                        <span class="result-number">${typeof item.result === 'number' ? item.result.toFixed(6) : item.result}</span>
                    </div>
                `;
            }

            return `
                <div class="history-item" onclick="repeatOperation(${index})">
                    <div class="history-item-header">
                        <span class="history-operation">
                            <i class="fas ${getOperationIcon(item.operation)}"></i>
                            ${item.operation}
                            <button class="repeat-btn" onclick="event.stopPropagation(); repeatOperation(${index})">
                                <i class="fas fa-repeat"></i> Повторить
                            </button>
                        </span>
                        <span class="history-time">${new Date().toLocaleString()}</span>
                    </div>
                    <div class="history-result">
                        ${resultDisplay}
                    </div>
                </div>
            `;
        }).join('');

    } catch (error) {
        console.error('Ошибка загрузки истории:', error);
        showToast('Не удалось загрузить историю', 'error');
    }
}

/**
 * ПОЛУЧИТЬ ИКОНКУ ДЛЯ ОПЕРАЦИИ
 * @param {string} operation - API имя операции
 * @returns {string} Класс иконки Font Awesome
 */
function getOperationIcon(operation) {
    const op = allOperations.find(o => o.apiName === operation);
    return op ? op.icon : 'fa-calculator';
}

/**
 * ПОВТОРИТЬ ОПЕРАЦИЮ ИЗ ИСТОРИИ
 * @param {number} index - Индекс операции в истории
 */
async function repeatOperation(index) {
    const item = currentHistory[index];
    if (!item) return;

    showToast(`Повтор операции: ${item.operation}`, 'info');

    // Восстанавливаем первую матрицу
    if (item.matrix1) {
        const rows1 = item.matrix1.rows;
        const cols1 = item.matrix1.cols;
        document.getElementById('rows1').value = rows1;
        document.getElementById('cols1').value = cols1;
        renderMatrix(1);
        fillMatrixData(1, item.matrix1);
        document.getElementById('checkA').checked = true;
    }

    // Восстанавливаем вторую матрицу (если была)
    if (item.matrix2) {
        const rows2 = item.matrix2.rows;
        const cols2 = item.matrix2.cols;
        document.getElementById('rows2').value = rows2;
        document.getElementById('cols2').value = cols2;
        renderMatrix(2);
        fillMatrixData(2, item.matrix2);
        document.getElementById('checkB').checked = true;
    } else {
        document.getElementById('checkB').checked = false;
    }

    updateUI();

    // Восстанавливаем выбранную операцию
    const select = document.getElementById('choosing-operation');
    for (let i = 0; i < select.options.length; i++) {
        if (select.options[i].value === item.operation) {
            select.selectedIndex = i;
            break;
        }
    }

    // Запускаем вычисление
    setTimeout(() => generateAPI(), 100);
}

/**
 * ОЧИСТКА ИСТОРИИ НА СЕРВЕРЕ
 */
async function clearHistory() {
    if (!confirm('Вы уверены, что хотите очистить всю историю вычислений?')) return;

    try {
        const response = await fetch('http://localhost:8080/calculate/history', { method: 'DELETE' });
        if (response.ok) {
            await refreshHistory();
            showToast('История очищена', 'success');
        } else {
            throw new Error('Ошибка очистки');
        }
    } catch (error) {
        showToast('Не удалось очистить историю', 'error');
    }
}

/**
 * ОТПРАВКА ЗАПРОСА НА СЕРВЕР ДЛЯ ВЫЧИСЛЕНИЯ
 *
 * ИЗМЕНЕНИЯ В ВЕРСИИ 2.1:
 * - Добавлена поддержка матрицы B для унарных операций
 * - Активная матрица (A или B) автоматически отправляется как matrix1
 */
async function generateAPI() {
    if (isLoading) {
        showToast('Подождите, выполняется предыдущий запрос...', 'info');
        return;
    }

    const operation = document.getElementById('choosing-operation').value;
    const checkA = document.getElementById('checkA').checked;
    const checkB = document.getElementById('checkB').checked;
    const operationName = document.querySelector('#choosing-operation option:checked')?.textContent || operation;

    if (!operation) {
        showToast('Выберите операцию!', 'error');
        return;
    }

    let url = '';
    let requestBody = {};

    const isBinary = ['ADD', 'SUB', 'MUL'].includes(operation);
    const isUnary = ['DET', 'TRACE', 'TRANSPOSE', 'INVERSE'].includes(operation);

    // Формируем запрос в зависимости от типа операции
    if (isBinary && checkA && checkB) {
        url = 'http://localhost:8080/calculate/binary';
        requestBody = {
            matrix1: getMatrixData(1),
            matrix2: getMatrixData(2),
            matrixOperation: operation
        };
    } else if (isUnary && (checkA || checkB)) {
        url = 'http://localhost:8080/calculate/unary';

        // ========== КЛЮЧЕВОЕ ИЗМЕНЕНИЕ ==========
        // Определяем, какая матрица активна (A или B)
        const activeMatrixNum = checkA ? 1 : 2;

        // Получаем данные активной матрицы
        const activeMatrixData = getMatrixData(activeMatrixNum);

        // Всегда отправляем как matrix1 (сервер ожидает matrix1 для унарных операций)
        requestBody = {
            matrix1: activeMatrixData,
            matrixOperation: operation
        };

        // Логируем для отладки (можно убрать в production)
        console.log(`Унарная операция ${operation} выполняется с матрицей ${activeMatrixNum === 1 ? 'A' : 'B'}`);
        // ========================================

    } else {
        showToast('Некорректный выбор операции или матриц!', 'error');
        return;
    }

    isLoading = true;
    const calcBtn = document.querySelector('.calculate-btn');
    const originalText = calcBtn.innerHTML;
    calcBtn.innerHTML = '<i class="fas fa-spinner fa-pulse"></i> Вычисление...';
    calcBtn.disabled = true;

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || error.detailMessage || 'Ошибка сервера');
        }

        const result = await response.json();
        showResult(result, operationName);
        showToast(`${operationName} выполнено!`, 'success');

        // Обновляем историю через полсекунды
        setTimeout(() => refreshHistory(), 500);

    } catch (error) {
        showToast(`Ошибка: ${error.message}`, 'error');
        console.error('Ошибка при выполнении операции:', error);
    } finally {
        isLoading = false;
        calcBtn.innerHTML = originalText;
        calcBtn.disabled = false;
    }
}

/**
 * ИНИЦИАЛИЗАЦИЯ ПРИ ЗАГРУЗКЕ СТРАНИЦЫ
 */
window.onload = async () => {
    renderMatrix(1);    // Отрисовываем матрицу A
    renderMatrix(2);    // Отрисовываем матрицу B
    updateUI();         // Настраиваем интерфейс
    await refreshHistory(); // Загружаем историю

    // Добавляем обработчики для чекбоксов, чтобы обновлять UI при их изменении
    const checkA = document.getElementById('checkA');
    const checkB = document.getElementById('checkB');

    if (checkA) {
        checkA.addEventListener('change', () => {
            updateUI();
            // Если выбрана унарная операция, показываем какая матрица активна
            const operation = document.getElementById('choosing-operation').value;
            const isUnary = ['DET', 'TRACE', 'TRANSPOSE', 'INVERSE'].includes(operation);
            if (isUnary && checkA.checked) {
                showToast('Выбрана матрица A для выполнения операции', 'info');
            }
        });
    }

    if (checkB) {
        checkB.addEventListener('change', () => {
            updateUI();
            // Если выбрана унарная операция, показываем какая матрица активна
            const operation = document.getElementById('choosing-operation').value;
            const isUnary = ['DET', 'TRACE', 'TRANSPOSE', 'INVERSE'].includes(operation);
            if (isUnary && checkB.checked) {
                showToast('Выбрана матрица B для выполнения операции', 'info');
            }
        });
    }
};