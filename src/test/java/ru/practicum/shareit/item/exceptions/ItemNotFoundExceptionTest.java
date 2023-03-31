package ru.practicum.shareit.item.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ItemNotFoundExceptionTest {

    @Test
    void testConstructor() {
        ItemNotFoundException actualItemNotFoundException = new ItemNotFoundException("An error occurred");
        assertNull(actualItemNotFoundException.getCause());
        assertEquals(0, actualItemNotFoundException.getSuppressed().length);
        assertEquals("An error occurred", actualItemNotFoundException.getMessage());
        assertEquals("An error occurred", actualItemNotFoundException.getLocalizedMessage());
    }
}

