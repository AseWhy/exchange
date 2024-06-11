package io.github.asewhy.exchange.base;

import io.github.asewhy.exchange.base.task.SampleData;
import io.github.asewhy.exchange.base.task.SampleProcessorService;
import io.github.asewhy.exchange.support.ExchangeExporterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Класс теста сервиса экспорта
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeProcessorServiceTest {

    /** Папка с артифактами */
    public static final String ARTIFACTS_FOLDER = "artifacts";

    /** Данные для экспорта */
    private List<SampleData> sampleData;

    @BeforeAll
    public void MakeTestData() throws IOException {
        var artifactsFolder = Path.of(ARTIFACTS_FOLDER);
        if (!Files.isDirectory(artifactsFolder)) {
            Files.createDirectory(artifactsFolder);
        } else {
            try (var stream = Files.list(artifactsFolder)) {
                stream.forEach(e -> e.toFile().delete());
            }
        }
        sampleData = List.of(
            new SampleData(0L, "Name 1"),
            new SampleData(1L, "Линия 1\nЛиния 2")
        );
    }

    @Test
    void TestExportCsv() throws Exception {
        var service = new SampleProcessorService();
        try (var output = new FileOutputStream(String.format("./%s/%s.csv", ARTIFACTS_FOLDER, System.currentTimeMillis()))) {
            service.export(sampleData, ExchangeExporterType.csv, Set.of("id", "name", "idName"), output);
        }
    }
}