package ru.practicum.shareit.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {
    /**
     * Method under test: {@link NotFoundException#NotFoundException(String)}
     */
    @Test
    void testConstructor() {
        NotFoundException actualNotFoundException = new NotFoundException("An error occurred");
        assertNull(actualNotFoundException.getCause());
        assertEquals(0, actualNotFoundException.getSuppressed().length);
        assertEquals("An error occurred", actualNotFoundException.getMessage());
        assertEquals("An error occurred", actualNotFoundException.getLocalizedMessage());
    }
}

