package ru.t1consulting.repository.impl;

import org.springframework.stereotype.Repository;
import ru.t1consulting.repository.CountResultRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

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

    public InMemoryCountResultRepositoryImpl(Clock clock) {
        this.cachedResults = new ArrayDeque<>(CAPACITY);
        this.resultsMap = new HashMap<>(CAPACITY);
        this.clock = clock;
    }

    @Override
    public String addNewResult(String word, String result) {
        removeExpired();

        CountResult countResult = new CountResult(word, result, Instant.now(clock));
        if (cachedResults.size() == CAPACITY) {
            resultsMap.remove(cachedResults.peek().word);
            cachedResults.poll();
        }
        cachedResults.add(countResult);
        resultsMap.put(word, result);

        return resultsMap.get(word);
    }

    @Override
    public String getIfExists(String word) {
        if (resultsMap.containsKey(word)) {
            return resultsMap.get(word);
        }
        return null;
    }

    private void removeExpired() {
        while (!cachedResults.isEmpty()
                && Duration.between(cachedResults.peek().requestInstant(), Instant.now(clock))
                .compareTo(Duration.of(TIME_AMOUNT, EXPIRATION_TIME_UNIT.toChronoUnit())) >= 0) {
            resultsMap.remove(cachedResults.peek().word);
            cachedResults.poll();
        }
    }

    private record CountResult(String word, String result, Instant requestInstant) { }
}
