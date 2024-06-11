package io.github.asewhy.exchange.base.task;

import com.opencsv.CSVWriter;
import io.github.asewhy.exchange.base.DefaultExchangeProcessorTaskProperties;
import io.github.asewhy.exchange.base.ExchangeProcessorService;
import io.github.asewhy.exchange.base.ExchangeProcessorTaskProperties;
import io.github.asewhy.exchange.base.ExchangeSimpleTaskFactory;
import io.github.asewhy.exchange.support.ExchangeExporterType;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис экспорта простых данных
 */
public class SampleProcessorService extends ExchangeProcessorService<SampleData, List<SampleData>> {

    /** Свойства экспортера для теста */
    private static final ExchangeProcessorTaskProperties DEFAULT_PROPERTIES = new DefaultExchangeProcessorTaskProperties(StandardCharsets.UTF_8, ';',
            CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END, "%.2f");

    /**
     * Конструктор
     */
    public SampleProcessorService() {
        super(new ExchangeSimpleTaskFactory<>(() -> new SampleExportTask(DEFAULT_PROPERTIES), SampleExportTask.class));
    }

    @Override
    public void export(List<SampleData> in, ExchangeExporterType type, Set<String> exportFields, OutputStream output) throws Exception {
        super.export(in, type, exportFields, output);
    }

    @Override
    protected Iterable<Collection<SampleData>> makeIterable(List<SampleData> data) {
        return data.stream().map(Collections::singleton).collect(Collectors.toList());
    }
}
