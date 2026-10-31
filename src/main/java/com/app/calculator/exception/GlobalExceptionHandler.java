package com.app.calculator.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ErrorResponse> handleArithmeticException(
            ArithmeticException ex,
            HttpServletRequest request) {
        return buildErrorResponse("ArithmeticError", ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        String message = "Missing required parameter: " + ex.getParameterName();
        return buildErrorResponse("MissingParameter", message, request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, NumberFormatException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleInvalidParameter(
            Exception ex,
            HttpServletRequest request) {
        String message = "Invalid numeric value provided.";
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            message = ex.getMessage();
        }
        return buildErrorResponse("InvalidParameter", message, request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String error,
            String message,
            String path,
            HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(error, message, status.value(), path));
    }

    public record ErrorResponse(String error, String message, int status, String path) {
    }
}
