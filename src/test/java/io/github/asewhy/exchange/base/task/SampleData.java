package io.github.asewhy.exchange.base.task;

/**
 * Данные для примера
 */
public class SampleData {

    /**
     * Идентификатор для примера
     */
    private final Long id;

    /**
     * Наименование для примера
     */
    private final String name;

    /**
     * Конструктор данных для примера
     *
     * @param id   идентификатор для примера
     * @param name наименование для примера
     */
    public SampleData(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Возвращает идентификатор для примера
     *
     * @return идентификатор для примера
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает наименование для примера
     *
     * @return наименование для примера
     */
    public String getName() {
        return name;
    }
}
