package io.github.asewhy.exchange.support;

import io.github.asewhy.exchange.base.ExchangeProcessorTask;

/**
 * Фабрика задач экспорта
 * @param <T> тип задач экспорта
 */
public interface ExchangeTaskFactory<T> {

    /**
     * Создать задачу
     * @return создать задачу
     */
    ExchangeProcessorTask<T> create();

    /**
     * Получить тип задачи
     * @return тип задачи
     */
    Class<? extends ExchangeProcessorTask<T>> getType();
}
