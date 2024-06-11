package io.github.asewhy.exchange.base;

import java.nio.charset.Charset;

/**
 * Свойства задачи экспорта
 */
public interface ExchangeProcessorTaskProperties {

    /**
     * Возвращает кодировку файла csv
     * @return кодировку файла csv
     */
    Charset getCsvCharset();

    /**
     * Возвращает разделительный символ
     * @return разделительный символ
     */
    char getCsvSeparator();

    /**
     * Возвращает символ кавычек
     * @return символ кавычек
     */
    char getCsvQuoteChar();

    /**
     * Возвращает символ экранирования
     * @return символ экранирования
     */
    char getCsvEscapeChar();

    /**
     * Возвращает символ конца строки
     * @return символ конца строки
     */
    String getCsvLineEnd();

    /**
     * Возвращает формат дробных чисел
     * @return формат дробных чисел
     */
    String getDoubleFormat();
}
