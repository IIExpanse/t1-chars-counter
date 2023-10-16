package ru.t1consulting.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.t1consulting.service.CharsCounterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chars")
@Validated
public class CharsCounterController {
    private final CharsCounterService service;

    /**
     * Подсчет количества символов в переданной строке.
     *
     * @param word - переданная на вход строка.
     *             Не может быть пустой и должна содержать как минимум один отображаемый символ.
     * @return результат подсчета символов в формате "“a”: 5, “c”: 4, “b”: 1",
     * отсортированный по убыванию количества вхождений символа в переданной строке.
     * @throws ConstraintViolationException - если переданная строка пустая или содержит
     *                                      только неотображаемые символы (пробелы, символы переноса строки и т.п.)
     */
    @GetMapping("count")
    public ResponseEntity<String> getCharsCount(@NotBlank @RequestParam String word) {
        return ResponseEntity.ok(service.getCharsCount(word));
    }
}
