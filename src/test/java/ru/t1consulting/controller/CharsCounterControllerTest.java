package ru.t1consulting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CharsCounterControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mvc;

    @Test
    void getCharsCount() throws Exception {
        String response = mvc.perform(
                        get(getDefaultUri())
                                .param("word", getTestString()))
                .andReturn().getResponse()
                .getContentAsString();
        assertEquals(getTestResult(), response);
    }

    @Test
    void shouldReturn400forBlankString() throws Exception {
        int response = mvc.perform(
                        get(getDefaultUri())
                                .param("word", " "))
                .andReturn().getResponse()
                .getStatus();
        assertEquals(400, response);
    }

    private String getDefaultUri() {
        return String.format("http://localhost:%d/chars/count", port);
    }

    private String getTestString() {
        return "aaaaa    bcccc";
    }

    private String getTestResult() {
        return "\"a\": 5, \"c\": 4, \"b\": 1";
    }
}