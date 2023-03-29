package ru.practicum.shareit.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BadRequestExceptionTest {
    /**
     * Method under test: {@link BadRequestException#BadRequestException(String)}
     */
    @Test
    void testConstructor() {
        BadRequestException actualBadRequestException = new BadRequestException("An error occurred");
        assertNull(actualBadRequestException.getCause());
        assertEquals(0, actualBadRequestException.getSuppressed().length);
        assertEquals("An error occurred", actualBadRequestException.getMessage());
        assertEquals("An error occurred", actualBadRequestException.getLocalizedMessage());
    }
}

