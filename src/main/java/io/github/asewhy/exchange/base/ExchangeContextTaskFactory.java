package io.github.asewhy.exchange.base;

import io.github.asewhy.exchange.support.ExchangeTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Фабрика задач, завязанная на контексте
 * @param <T> тип задачи
 */
public class ExchangeContextTaskFactory<T> implements ExchangeTaskFactory<T> {

    /** Контекст */
    @Autowired
    protected ApplicationContext context;

    /** Тип задачи */
    protected final Class<? extends ExchangeProcessorTask<T>> taskType;

    /**
     * Конструктор фабрики
     * @param taskType тип задачи
     */
    public ExchangeContextTaskFactory(Class<? extends ExchangeProcessorTask<T>> taskType) {
        this.taskType = taskType;
    }

    @Override
    public ExchangeProcessorTask<T> create() {
        return context.getBean(taskType);
    }

    @Override
    public Class<? extends ExchangeProcessorTask<T>> getType() {
        return taskType;
    }
}
