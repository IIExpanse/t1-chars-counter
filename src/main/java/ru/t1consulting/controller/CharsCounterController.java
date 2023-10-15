package ru.t1consulting.controller;

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

    @GetMapping("count")
    public ResponseEntity<String> getCharsCount(@NotBlank @RequestParam String word) {
        return ResponseEntity.ok(service.getCharsCount(word));
    }
}
