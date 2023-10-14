package ru.t1consulting.repository.impl;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AllArgsConstructor(onConstructor_ = @Autowired)
class InMemoryCountResultRepositoryImplTest {
    private InMemoryCountResultRepositoryImpl repository;

    @Test
    void addNewResult() {
        String res = getTestResult();
        assertEquals(res, repository.addNewResult(getTestString(), res));
    }

    @Test
    void getIfExists() {
        String word = getTestString();
        assertNull(repository.getIfExists(word));

        String res = getTestResult();
        repository.addNewResult(word, res);
        assertEquals(res, repository.getIfExists(word));
    }

    @Test
    void testExpiredRemoval() {
        repository = new InMemoryCountResultRepositoryImpl(getTestClock());

        repository.addNewResult(getTestString(), getTestResult());
        repository.addNewResult(getTestString() + "a", getTestResult());
        assertNull(repository.getIfExists(getTestString()));
    }

    private Clock getTestClock() {
        return new Clock() {
            final Instant[] instants = new Instant[]{Instant.now(), Instant.MAX};
            int idx = 0;

            @Override
            public ZoneId getZone() {
                return Clock.systemDefaultZone().getZone();
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return Clock.systemDefaultZone();
            }

            @Override
            public Instant instant() {
                if (idx == 2) {
                    idx = 0;
                }
                return instants[idx++];
            }
        };
    }

    private String getTestString() {
        return "aaaaabcccc";
    }

    private String getTestResult() {
        return "“a”: 5, “c”: 4, “b”: 1";
    }
}