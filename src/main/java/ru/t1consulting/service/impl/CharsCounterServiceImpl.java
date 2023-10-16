package ru.t1consulting.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1consulting.repository.CountResultRepository;
import ru.t1consulting.service.CharsCounterService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharsCounterServiceImpl implements CharsCounterService {
    private final CountResultRepository repository;

    /**
     * Метод для возврата результата подсчета символов в строке.
     * Перед вызовом метода подсчета проверяет, содержится ли данная строка в кэше приложения.
     *
     * @param word - переданная на вход строка.
     * @return результат подсчета символов в формате "“a”: 5, “c”: 4, “b”: 1",
     */
    @Override
    public String getCharsCount(String word) {
        return repository.getIfExists(word)
                .orElseGet(() -> repository.addNewResult(word, countChars(word)));
    }

    /**
     * Метод для подсчета количества символов и вывода отсортированного результата.
     * Игнорирует неотображаемые символы.
     *
     * @param word - переданная на вход строка.
     * @return результат подсчета символов в формате "“a”: 5, “c”: 4, “b”: 1",
     */
    private String countChars(String word) {
        Map<Character, Integer> entries = new HashMap<>();
        Map<Integer, Queue<Character>> wordsByCounts = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        word.chars()
                .filter(c -> !Character.isWhitespace((char) c))
                .forEach(c -> entries.compute((char) c, (key, val) -> val == null ? 1 : val + 1));

        entries.forEach((key, value) -> {
            wordsByCounts.computeIfAbsent(value, (val) -> new PriorityQueue<>());
            wordsByCounts.get(value).add(key);
        });
        wordsByCounts.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .forEach(key -> {
                    Queue<Character> queue = wordsByCounts.get(key);

                    while (!queue.isEmpty()) {
                        sb.append("\"").append(queue.poll()).append("\"").append(": ");
                        sb.append(key).append(", ");
                    }
                });
        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();
    }
}
