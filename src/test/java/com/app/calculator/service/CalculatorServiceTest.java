package com.app.calculator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void addShouldReturnSum() {
        assertEquals(15.0, calculatorService.add(10, 5));
    }

    @Test
    void subtractShouldReturnDifference() {
        assertEquals(5.0, calculatorService.subtract(10, 5));
    }

    @Test
    void multiplyShouldReturnProduct() {
        assertEquals(50.0, calculatorService.multiply(10, 5));
    }

    @Test
    void divideShouldReturnQuotient() {
        assertEquals(2.0, calculatorService.divide(10, 5));
    }

    @Test
    void divideByZeroShouldThrowArithmeticException() {
        ArithmeticException exception = assertThrows(ArithmeticException.class,
                () -> calculatorService.divide(10, 0));

        assertEquals("Division by zero is not allowed.", exception.getMessage());
    }
}
