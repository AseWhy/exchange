package io.github.asewhy.exchange.base;

import com.opencsv.CSVWriter;
import io.github.asewhy.ReflectionUtils;
import io.github.asewhy.exchange.support.ExchangeExporterType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Задача экспорта
 * @param <T> тип чанков
 */
public abstract class ExchangeProcessorTask<T> {

    /** Свойства по умолчанию */
    private static final ExchangeProcessorTaskProperties DEFAULT_PROPERTIES = new DefaultExchangeProcessorTaskProperties(Charset.forName("Windows-1251"), ';',
            CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END, "%.2f");

    /** Набор полей для экспорта */
    private final Collection<String> fields;

    /** Свойства задачи экспорта */
    private final ExchangeProcessorTaskProperties properties;

    /** Геттеры значений для полей */
    protected Map<String, Method> accessible;

    /** Итератор чанков для экспорта */
    protected Iterable<Collection<T>> chunks;

    /**
     * Конструктор задачи экспорта
     * @param properties свойства задачи экспорта
     * @param fields набор полей для экспорта
     */
    public ExchangeProcessorTask(ExchangeProcessorTaskProperties properties, @NotNull String... fields) {
        this.fields = new ArrayList<>(List.of(fields));
        this.properties = properties;
    }

    /**
     * Конструктор задачи экспорта со свойствами по умолчанию
     * @param fields набор полей для экспорта
     */
    public ExchangeProcessorTask(@NotNull String... fields) {
        this.fields = new ArrayList<>(List.of(fields));
        this.properties = DEFAULT_PROPERTIES;
    }

    /**
     * Обрабатывает кусок перед очисткой
     * @param chunk кусок
     */
    protected abstract void processFlushChunk(Collection<T> chunk);

    /**
     * Обрабатывает кусок перед получением
     * @param chunk кусок
     */
    protected abstract void processReceiveChunk(Collection<T> chunk);

    /**
     * Поставляет данные для выполнения задачи
     * @param chunks итератор чанков для экспорта
     * @param exportFields названия полей для экспорта
     * @param accessible информация о полях задачи
     */
    public void bind(
        Iterable<Collection<T>> chunks,
        Set<String> exportFields,
        Map<String, Method> accessible
    ) {
        this.chunks = chunks;
        this.accessible = accessible;

        if(exportFields != null) {
            this.fields.removeIf(e -> !exportFields.contains(e));
        }
    }

    /**
     * Возвращает геттер для поля name
     * @param name название поля
     * @return найденный геттер или null
     */
    private Method getAccessible(String name) {
        var found = accessible.get(name);

        if(found == null) {
            found = accessible.get("get" + StringUtils.capitalize(name));
            accessible.put(name, found);
        }

        if(found == null) {
            found = accessible.get("get" + NameUtil.upperCamelCase(StringUtils.capitalize(name)));
            accessible.put(name, found);
        }

        return found;
    }

    /**
     * Возвращает локализированное название поля
     * @param name изначальное название поля
     * @return локализированное название поля
     */
    private String getColumnName(String name) {
        var found = getAccessible(name);

        if(found == null || !found.isAnnotationPresent(Column.class)) {
            throw new RuntimeException("Accessible not found for field " + name + " [get" + StringUtils.capitalize(name) + "]");
        }

        return found.getAnnotation(Column.class).value();
    }

    /**
     * Возвращает строковое значение столбца name для объекта of
     * @param name название столбца для получения значения
     * @param of объект для получения значения для столбца name
     * @return значение столбца name
     */
    private Object getValue(String name, T of) {
        var found = getAccessible(name);

        if(found == null || !found.isAnnotationPresent(Column.class)) {
            throw new RuntimeException("Accessible not found for field " + name + " [get" + StringUtils.capitalize(name) + "]");
        }

        return ReflectionUtils.safeInvoke(found, this, of);
    }

    /**
     * Конвертирует сырой результат выполнения геттера в строковое значение столбца
     * @param value сырой результат
     * @param field название поля, для которого было получено значение
     * @return конвертированное значение пригодное для выведения пользователю
     */
    private String convertValue(Object value, String field) {
        if(value instanceof String) {
            return StringEscapeUtils.escapeCsv((String) value);
        }

        if(value instanceof Double) {
            return String.format(properties.getDoubleFormat(), value);
        }

        if(value instanceof Number) {
            return value.toString();
        }

        if(value == null) {
            return "";
        }

        throw new RuntimeException("Cannot detect type of result for field " + field);
    }

    /**
     * Обрабатывает экспорт в csv
     * @param output поток вывода для экспорта
     * @throws IOException при ошибке вывода
     */
    private void handleCsv(OutputStream output) throws IOException {
        try(
            var writer = new CSVWriter(new OutputStreamWriter(output, properties.getCsvCharset()), properties.getCsvSeparator(), properties.getCsvQuoteChar(),
                    properties.getCsvEscapeChar(), properties.getCsvLineEnd())
        ) {
            handleCsvWriteHeader(writer);
            handleCsvData(writer);
        }
    }

    /**
     * Выводит заголовок на лист scv
     * @param writer писатель scv
     */
    private void handleCsvWriteHeader(@NotNull CSVWriter writer) {
        var index = 0;
        var header = new String[fields.size()];

        for(var field: fields) {
            header[index++] = getColumnName(field);
        }

        writer.writeNext(header);
    }

    /**
     * Выводит данные на csv лист
     * @param writer писатель csv
     */
    private void handleCsvData(@NotNull CSVWriter writer) {
        var data = new String[fields.size()];

        for(var chunk: chunks) {
            processReceiveChunk(chunk);

            try {
                for(var current: chunk) {
                    var index = 0;

                    for (var field : fields) {
                        data[index++] = convertValue(getValue(field, current), field);
                    }

                    writer.writeNext(data);
                }
            } finally {
                processFlushChunk(chunk);
            }
        }
    }

    /**
     * Обрабатывает экспорт в xlsx
     * @param output поток вывода для экспорта
     * @throws IOException при ошибке вывода
     */
    private void handleXlsx(OutputStream output) throws IOException {
        try(var workbook = new XSSFWorkbook()) {
            var styles = workbook.createCellStyle();
            var sheet = workbook.createSheet();

            sheet.setAutobreaks(true);

            styles.setWrapText(true);

            handleXlsxWriteHeader(sheet, styles);
            handleXlsxWriteData(sheet, styles);
            handleXlsxResizeColumns(sheet, styles);

            workbook.write(output);
        }
    }

    /**
     * Выводит заголовок xlsx экспорта списка
     * @param sheet лист
     * @param styles стили
     */
    private void handleXlsxWriteHeader(@NotNull XSSFSheet sheet, XSSFCellStyle styles) {
        var row = sheet.createRow(0);
        var cellIndex = 0;

        for(var field: fields) {
            var cell = row.createCell(cellIndex);

            cell.setCellValue(getColumnName(field));

            cellIndex++;
        }
    }

    /**
     * Выводит данные на xlsx лист
     * @param sheet лист
     * @param styles стили
     */
    private void handleXlsxWriteData(@NotNull XSSFSheet sheet, XSSFCellStyle styles) {
        var rowIndex = 1;

        for(var chunk: chunks) {
            processReceiveChunk(chunk);

            try {
                for(var current: chunk) {
                    var row = sheet.createRow(rowIndex++);

                    var cellIndex = 0;

                    for (var field : fields) {
                        var cell = row.createCell(cellIndex);
                        var value = getValue(field, current);

                        if(value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if(value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if(value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if(value instanceof Double) {
                            cell.setCellValue((Double) value);
                        } else {
                            cell.setCellValue(convertValue(value, field));
                        }

                        cellIndex++;
                    }
                }
            } finally {
                processFlushChunk(chunk);
            }
        }
    }

    /**
     * Устанавливает максимальную ширину столбцев, чтобы они не выгладили так монструозно
     * @param sheet лист
     */
    private void handleXlsxResizeColumns(XSSFSheet sheet, XSSFCellStyle styles) {
        for (int i = 0; i < fields.size(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setDefaultColumnStyle(i, styles);

            if (sheet.getColumnWidth(i) > 17500) {
                sheet.setColumnWidth(i, 17500);
            }
        }
    }

    /**
     * Обрабатывает экспорт для типа type и потока output
     * @param type тип экспорта
     * @param output поток вывода
     * @throws Exception если произошла ошибка
     */
    protected final void handle(ExchangeExporterType type, OutputStream output) throws Exception {
        if(type == ExchangeExporterType.csv) {
            handleCsv(output);
        } else if(type == ExchangeExporterType.xlsx) {
            handleXlsx(output);
        }
    }
}
