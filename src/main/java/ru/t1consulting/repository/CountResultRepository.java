package ru.t1consulting.repository;

public interface CountResultRepository {

    String addNewResult(String word, String result);

    String getIfExists(String word);
}
