package io.github.asewhy.exchange.support;

import java.io.OutputStream;
import java.util.Set;

/**
 * Обработчик экспорта
 * @param <T> тип входящих данных
 */
public interface ExchangeProcessor<T> {

    /**
     * Экспортирует список сущностей в таблицу excel или csv
     * @param data фильтр для экспорта
     * @param type тип экспорта
     * @param exportFields названия полей для экспорта
     * @param output поток для вывода
     * @throws Exception если при экспорте произошли ошибки
     */
    void export(T data, ExchangeExporterType type, Set<String> exportFields, OutputStream output) throws Exception;
}
