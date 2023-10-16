package ru.t1consulting.repository.impl;

import org.springframework.stereotype.Repository;
import ru.t1consulting.repository.CountResultRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Репозиторий, реализующий кэширование фиксированного количества результатов обработки строк в памяти приложения.
 * Удаляет результаты обработки из памяти при превышении лимита хранения либо истечения срока хранения.
 */
@Repository
public class InMemoryCountResultRepositoryImpl implements CountResultRepository {
    private final Queue<CountResult> cachedResults;
    private final Map<String, String> resultsMap;
    private final Clock clock;
    public static final int CAPACITY = 100;
    private final TimeUnit EXPIRATION_TIME_UNIT = TimeUnit.DAYS;
    private final int TIME_AMOUNT = 1;

    public InMemoryCountResultRepositoryImpl() {
        this.cachedResults = new ArrayDeque<>(CAPACITY);
        this.resultsMap = new HashMap<>(CAPACITY);
        this.clock = Clock.systemDefaultZone();
    }

    /**
     * Конструктор для тестирования функционала по удалению данных с истекшим сроком хранения.
     */
    public InMemoryCountResultRepositoryImpl(Clock clock) {
        this.cachedResults = new ArrayDeque<>(CAPACITY);
        this.resultsMap = new HashMap<>(CAPACITY);
        this.clock = clock;
    }

    /**
     * Сохранение готового результата и фиксация времени сохранения.
     *
     * @param word - переданная на вход строка.
     * @param result - результат подсчета символов в строке.
     * @return сохраненный результат подсчета,
     */
    @Override
    public String addNewResult(String word, String result) {
        removeExpired();

        CountResult countResult = new CountResult(word, Instant.now(clock));
        if (cachedResults.size() == CAPACITY) {
            resultsMap.remove(cachedResults.peek().word);
            cachedResults.poll();
        }
        cachedResults.add(countResult);
        resultsMap.put(word, result);

        return resultsMap.get(word);
    }

    /**
     * Проверка наличия результата подсчета строки в кэше.
     *
     * @param word - переданная на вход строка.
     * @return Optional, содержащий кэшированный результат или null ри его отсутствии.
     */
    @Override
    public Optional<String> getIfExists(String word) {
        if (resultsMap.containsKey(word)) {
            return Optional.of(resultsMap.get(word));
        }
        return Optional.empty();
    }

    /**
     * Инициализация удаления кэшированных данных с истекшим сроком хранения (если есть).
     */
    private void removeExpired() {
        while (!cachedResults.isEmpty()
                && Duration.between(cachedResults.peek().requestInstant(), Instant.now(clock))
                .compareTo(Duration.of(TIME_AMOUNT, EXPIRATION_TIME_UNIT.toChronoUnit())) >= 0) {
            resultsMap.remove(cachedResults.peek().word);
            cachedResults.poll();
        }
    }

    private record CountResult(String word, Instant requestInstant) { }
}
