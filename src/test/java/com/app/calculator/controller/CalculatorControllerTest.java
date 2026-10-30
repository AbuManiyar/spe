package com.app.calculator.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CalculatorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void addShouldReturn200AndExpectedJson() throws Exception {
        mockMvc.perform(get("/api/v1/add").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                .andExpect(jsonPath("$.result").value(15.0));
    }

    @Test
    void subtractShouldReturn200AndExpectedJson() throws Exception {
        mockMvc.perform(get("/api/v1/subtract").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("subtract"))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                .andExpect(jsonPath("$.result").value(5.0));
    }

    @Test
    void multiplyShouldReturn200AndExpectedJson() throws Exception {
        mockMvc.perform(get("/api/v1/multiply").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("multiply"))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                .andExpect(jsonPath("$.result").value(50.0));
    }

    @Test
    void divideShouldReturn200AndExpectedJson() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "10").param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("divide"))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                .andExpect(jsonPath("$.result").value(2.0));
    }

    @Test
    void divideByZeroShouldReturn400AndErrorPayload() throws Exception {
        mockMvc.perform(get("/api/v1/divide").param("a", "10").param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ArithmeticError"))
                .andExpect(jsonPath("$.message").value("Division by zero is not allowed."))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/divide"));
    }
}
