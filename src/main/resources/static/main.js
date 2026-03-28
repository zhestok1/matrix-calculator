/**
 * КОНФИГУРАЦИЯ ОПЕРАЦИЙ
 */
const allOperations = [
    { name: "Сумма матриц", type: "double", apiName: "ADD", icon: "fa-plus" },
    { name: "Вычитание матриц", type: "double", apiName: "SUB", icon: "fa-minus" },
    { name: "Умножение матриц", type: "double", apiName: "MUL", icon: "fa-times" },
    { name: "Определитель матрицы", type: "single", apiName: "DET", icon: "fa-calculator" },
    { name: "След матрицы", type: "single", apiName: "TRACE", icon: "fa-chart-line" }
];

let isLoading = false;
let currentHistory = [];

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
 * ОТРИСОВКА МАТРИЦЫ
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
 * СБОР ДАННЫХ МАТРИЦЫ
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
 * ЗАПОЛНЕНИЕ МАТРИЦЫ ДАННЫМИ
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
 * ОЧИСТКА МАТРИЦ
 */
function clearMatrices() {
    [1, 2].forEach(num => {
        const inputs = document.querySelectorAll(`#matrix${num} .matrix-cell`);
        inputs.forEach(input => input.value = '0');
    });
    showToast('Матрицы очищены', 'success');
}

/**
 * ОБРАБОТЧИК ИЗМЕНЕНИЯ ОПЕРАЦИИ
 */
function onOperationChange() {
    const operation = document.getElementById('choosing-operation').value;
    const checkA = document.getElementById('checkA').checked;
    const checkB = document.getElementById('checkB').checked;
    const isUnary = ['DET', 'TRACE'].includes(operation);
    const isBinary = ['ADD', 'SUB', 'MUL'].includes(operation);
    const activeMatricesCount = (checkA ? 1 : 0) + (checkB ? 1 : 0);

    if (isUnary && activeMatricesCount === 2) {
        document.getElementById('checkB').checked = false;
        updateUI();
        showToast('Для унарной операции активна только матрица A', 'info');
    }

    if (isBinary && activeMatricesCount === 1) {
        document.getElementById('checkA').checked = true;
        document.getElementById('checkB').checked = true;
        updateUI();
        showToast('Для бинарной операции активны обе матрицы', 'info');
    }
}

/**
 * УПРАВЛЕНИЕ ИНТЕРФЕЙСОМ
 */
function updateUI() {
    const checkA = document.getElementById('checkA').checked;
    const checkB = document.getElementById('checkB').checked;
    const select = document.getElementById('choosing-operation');
    const wrapper1 = document.getElementById('wrapper1');
    const wrapper2 = document.getElementById('wrapper2');
    const exchangeBtn = document.getElementById('matrix-exchange');
    const clearBtn = document.getElementById('clear-matrices');

    if (wrapper1) wrapper1.classList.toggle('hidden', !checkA);
    if (wrapper2) wrapper2.classList.toggle('hidden', !checkB);

    if (exchangeBtn) exchangeBtn.style.display = (checkA && checkB) ? 'flex' : 'none';
    if (clearBtn) clearBtn.style.display = (checkA || checkB) ? 'flex' : 'none';

    const currentSelected = select.value;

    select.innerHTML = '<option value="" selected disabled>-- Выберите операцию --</option>';

    const activeMatricesCount = (checkA ? 1 : 0) + (checkB ? 1 : 0);

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

    if (currentSelected && [...select.options].some(opt => opt.value === currentSelected)) {
        select.value = currentSelected;
    }
}

/**
 * ОБМЕН МАТРИЦ
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
 * ФОРМАТИРОВАНИЕ МАТРИЦЫ В HTML ТАБЛИЦУ
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
 * ПОКАЗ РЕЗУЛЬТАТА
 */
function showResult(result, operation) {
    const resultCard = document.getElementById('result-card');
    const resultContent = document.getElementById('result-content');

    if (!resultContent) return;

    let formattedHtml = '';

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
    } else {
        formattedHtml = `<div class="result-value">${typeof result === 'number' ? result.toFixed(6) : result}</div>`;
    }

    resultContent.innerHTML = formattedHtml;
    resultCard.style.display = 'block';
    resultCard.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

/**
 * ЗАКРЫТЬ РЕЗУЛЬТАТ
 */
function closeResult() {
    document.getElementById('result-card').style.display = 'none';
}

/**
 * ПОКАЗ УВЕДОМЛЕНИЯ
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

const toastStyle = document.createElement('style');
toastStyle.textContent = `
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(toastStyle);

/**
 * ЗАГРУЗКА ИСТОРИИ (с отображением матриц)
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

            // Проверяем, является ли результат матрицей
            if (item.result && typeof item.result === 'object' && item.result.data) {
                // Отображаем матрицу в виде красивой таблицы
                resultDisplay = `
                    <div class="history-matrix-wrapper">
                        <strong>Результат (${item.result.rows}×${item.result.cols}):</strong>
                        <div style="margin-top: 8px;">
                            ${formatMatrixAsTable(item.result)}
                        </div>
                    </div>
                `;
            } else {
                // Для чисел (определитель, след)
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
 */
function getOperationIcon(operation) {
    const op = allOperations.find(o => o.apiName === operation);
    return op ? op.icon : 'fa-calculator';
}

/**
 * ПОВТОРИТЬ ОПЕРАЦИЮ ИЗ ИСТОРИИ
 */
async function repeatOperation(index) {
    const item = currentHistory[index];
    if (!item) return;

    showToast(`Повтор операции: ${item.operation}`, 'info');

    if (item.matrix1) {
        const rows1 = item.matrix1.rows;
        const cols1 = item.matrix1.cols;
        document.getElementById('rows1').value = rows1;
        document.getElementById('cols1').value = cols1;
        renderMatrix(1);
        fillMatrixData(1, item.matrix1);
        document.getElementById('checkA').checked = true;
    }

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

    const select = document.getElementById('choosing-operation');
    for (let i = 0; i < select.options.length; i++) {
        if (select.options[i].value === item.operation) {
            select.selectedIndex = i;
            break;
        }
    }

    setTimeout(() => generateAPI(), 100);
}

/**
 * ОЧИСТКА ИСТОРИИ
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
 * ОТПРАВКА ЗАПРОСА
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
    const isUnary = ['DET', 'TRACE'].includes(operation);

    if (isBinary && checkA && checkB) {
        url = 'http://localhost:8080/calculate/binary';
        requestBody = {
            matrix1: getMatrixData(1),
            matrix2: getMatrixData(2),
            matrixOperation: operation
        };
    } else if (isUnary && (checkA || checkB)) {
        url = 'http://localhost:8080/calculate/unary';
        const matrixNum = checkA ? 1 : 2;
        requestBody = {
            matrix1: getMatrixData(matrixNum),
            matrixOperation: operation
        };
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

        setTimeout(() => refreshHistory(), 500);

    } catch (error) {
        showToast(`Ошибка: ${error.message}`, 'error');
    } finally {
        isLoading = false;
        calcBtn.innerHTML = originalText;
        calcBtn.disabled = false;
    }
}

/**
 * ИНИЦИАЛИЗАЦИЯ
 */
window.onload = async () => {
    renderMatrix(1);
    renderMatrix(2);
    updateUI();
    await refreshHistory();
};