package io.github.asewhy.exchange.base;

import io.github.asewhy.ReflectionUtils;
import io.github.asewhy.exchange.support.ExchangeExporterType;
import io.github.asewhy.exchange.support.ExchangeProcessor;
import io.github.asewhy.exchange.support.ExchangeTaskFactory;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Абстрактный сервис экспорта
 * @param <T> тип задачи экспорта
 * @param <I> тип входящих данных
 */
public abstract class ExchangeProcessorService<T, I> implements ExchangeProcessor<I> {

    /** Фабрика задач экспорта */
    protected ExchangeTaskFactory<T> taskFactory;

    /** Набор геттеров сервиса экспорта */
    protected Map<String, Method> taskAccessible;

    /**
     * Конструктор сервиса экспорта
     * @param taskFactory фабрика задач экспорта
     */
    public ExchangeProcessorService(ExchangeTaskFactory<T> taskFactory) {
        this.taskFactory = taskFactory;
        this.taskAccessible = ReflectionUtils.scanMethodsToMap(taskFactory.getType());
    }

    @Override
    public void export(
        I in,
        ExchangeExporterType type,
        Set<String> exportFields,
        OutputStream output
    ) throws Exception {
        var task = taskFactory.create();

        task.bind(
            makeIterable(in),
            exportFields,
            taskAccessible
        );

        task.handle(type, output);
    }

    protected abstract Iterable<Collection<T>> makeIterable(I data);
}
