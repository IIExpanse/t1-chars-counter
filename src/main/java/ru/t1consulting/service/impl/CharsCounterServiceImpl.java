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

    @Override
    public String getCharsCount(String word) {
        String count = repository.getIfExists(word);
        if (count != null) {
            return count;
        }

        return repository.addNewResult(word, countChars(word));
    }

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
