package ru.practicum.shareit.advice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.NotFoundException;

import static org.mockito.Mockito.*;

class GlobalErrorAdviceTest {
    @Mock
    Logger log;
    @InjectMocks
    GlobalErrorAdvice globalErrorAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNotFoundExceptionHandler() {
        GlobalErrorAdvice.ErrorResponse result = globalErrorAdvice.notFoundExceptionHandler(new NotFoundException("Error"));
        Assertions.assertEquals(result.getError(), "Error");
    }

    @Test
    void testThrowableHandler() {
        GlobalErrorAdvice.ErrorResponse result = globalErrorAdvice.throwableHandler(new NotFoundException("Error"));
        Assertions.assertEquals(result.getError(), "Error");
    }

    @Test
    void testMethodArgumentNotValidExceptionHandler() {
        GlobalErrorAdvice.ErrorResponse result = globalErrorAdvice.
                methodArgumentNotValidExceptionHandler(new DataIntegrityViolationException("Error"));
        Assertions.assertEquals(result.getError(), "Error");
    }

    @Test
    void testBadRequestExceptionHandler() {
        GlobalErrorAdvice.ErrorResponse result = globalErrorAdvice.badRequestExceptionHandler(new NotFoundException("Error"));
        Assertions.assertEquals(result.getError(), "Error");
    }
}
