package ru.t1consulting.service.impl;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AllArgsConstructor(onConstructor_ = @Autowired)
class CharsCounterServiceImplTest {
    private CharsCounterServiceImpl service;

    @Test
    void getCharsCount() {
        assertEquals("\"a\": 5, \"c\": 4, \"b\": 1", service.getCharsCount("aaaaabcccc"));
        assertEquals("\"a\": 5, \"c\": 4, \"d\": 3, \"b\": 1", service.getCharsCount("aaadddaabcccc"));
        assertEquals("\"a\": 5, \"b\": 4, \"c\": 4, \"d\": 4", service.getCharsCount("aabbbaddddaabcccc"));
    }
}