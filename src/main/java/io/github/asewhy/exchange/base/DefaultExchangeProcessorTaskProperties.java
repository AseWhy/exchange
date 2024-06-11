package io.github.asewhy.exchange.base;

import java.nio.charset.Charset;

/**
 * Реализация свойств по умолчанию
 */
public class DefaultExchangeProcessorTaskProperties implements ExchangeProcessorTaskProperties {

    /** Кодировка файла csv */
    private final Charset csvCharset;

    /** Раделитель столбцов файла csv */
    private final char csvSeparator;

    /** Символ кавычекк файлов csv */
    private final char csvQuoteChar;

    /** Символ экранирования файла csv */
    private final char csvEscapeChar;

    /** Символ конца строки файла csv */
    private final String csvLineEnd;

    /** Формат дробных чисел */
    private final String doubleFormat;

    /**
     * Конструктор реалзиации свойств по цмолчанию
     * @param csvCharset кодировка файла csv
     * @param csvSeparator раделитель столбцов файла csv
     * @param csvQuoteChar символ кавычекк файлов csv
     * @param csvEscapeChar символ экранирования файла csv
     * @param csvLineEnd символ конца строки файла csv
     * @param doubleFormat формат дробных чисел
     */
    public DefaultExchangeProcessorTaskProperties(Charset csvCharset, char csvSeparator, char csvQuoteChar, char csvEscapeChar, String csvLineEnd, String doubleFormat) {
        this.csvCharset = csvCharset;
        this.csvSeparator = csvSeparator;
        this.csvQuoteChar = csvQuoteChar;
        this.csvEscapeChar = csvEscapeChar;
        this.csvLineEnd = csvLineEnd;
        this.doubleFormat = doubleFormat;
    }

    @Override
    public Charset getCsvCharset() {
        return csvCharset;
    }

    @Override
    public char getCsvSeparator() {
        return csvSeparator;
    }

    @Override
    public char getCsvQuoteChar() {
        return csvQuoteChar;
    }

    @Override
    public char getCsvEscapeChar() {
        return csvEscapeChar;
    }

    @Override
    public String getCsvLineEnd() {
        return csvLineEnd;
    }

    @Override
    public String getDoubleFormat() {
        return doubleFormat;
    }
}
