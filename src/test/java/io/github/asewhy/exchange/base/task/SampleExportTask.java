package io.github.asewhy.exchange.base.task;

import io.github.asewhy.exchange.base.Column;
import io.github.asewhy.exchange.base.ExchangeProcessorTask;
import io.github.asewhy.exchange.base.ExchangeProcessorTaskProperties;

import java.util.Collection;

/** Задача экспорта */
public class SampleExportTask extends ExchangeProcessorTask<SampleData> {

    /**
     * Конструктор задачи экспорта
     * @param properties свойства задачи экспорта
     */
    public SampleExportTask(ExchangeProcessorTaskProperties properties) {
        super(properties, "id", "name", "idName");
    }

    /**
     * Возвращает идентификатор простых данных
     * @param data простые данные
     * @return идентификатор
     */
    @Column("Идентификатор")
    public Long getId(SampleData data) {
        return data.getId();
    }

    /**
     * Возвращает наименование простых данных
     * @param data простые данные
     * @return наименование
     */
    @Column("Наименование")
    public String getName(SampleData data) {
        return data.getName();
    }

    /**
     * Возвращает идентификатор и наименование простых данных
     * @param data простые данные
     * @return идентификатор и наименование
     */
    @Column("Идентификатор и наименование")
    public String getIdName(SampleData data) {
        return data.getId() + " " + data.getName();
    }

    @Override
    protected void processFlushChunk(Collection<SampleData> chunk) {

    }

    @Override
    protected void processReceiveChunk(Collection<SampleData> chunk) {

    }
}
