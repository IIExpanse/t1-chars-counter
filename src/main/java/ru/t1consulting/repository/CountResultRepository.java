package ru.t1consulting.repository;

import java.util.Optional;

public interface CountResultRepository {

    String addNewResult(String word, String result);

    Optional<String> getIfExists(String word);
}
