package ru.practicum.shareit.user.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UserNotFoundExceptionTest {
    /**
     * Method under test: {@link UserNotFoundException#UserNotFoundException(String)}
     */
    @Test
    void testConstructor() {
        UserNotFoundException actualUserNotFoundException = new UserNotFoundException("An error occurred");
        assertNull(actualUserNotFoundException.getCause());
        assertEquals(0, actualUserNotFoundException.getSuppressed().length);
        assertEquals("An error occurred", actualUserNotFoundException.getMessage());
        assertEquals("An error occurred", actualUserNotFoundException.getLocalizedMessage());
    }
}

