package io.github.asewhy.exchange.base;

import io.github.asewhy.exchange.support.ExchangeTaskFactory;

import java.util.function.Supplier;

/**
 * Класс фабрики задач
 * @param <T> тип задачи
 */
public class ExchangeSimpleTaskFactory<T> implements ExchangeTaskFactory<T> {

    /** Метод, возвращающий задачу экспорта */
    protected final Supplier<ExchangeProcessorTask<T>> supplier;

    /** Тип задачи */
    protected final Class<? extends ExchangeProcessorTask<T>> taskType;

    /**
     * Конструктор простой фабрики задач
     * @param supplier метод, возвращающий новую задачу экспорта
     * @param taskType тип задачи
     */
    public ExchangeSimpleTaskFactory(Supplier<ExchangeProcessorTask<T>> supplier, Class<? extends ExchangeProcessorTask<T>> taskType) {
        this.supplier = supplier;
        this.taskType = taskType;
    }

    @Override
    public ExchangeProcessorTask<T> create() {
        return supplier.get();
    }

    @Override
    public Class<? extends ExchangeProcessorTask<T>> getType() {
        return taskType;
    }
}
