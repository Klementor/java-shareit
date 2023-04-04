package ru.practicum.shareit.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testConstructor() {
        User actualUser = new User();
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setId(1L);
        actualUser.setName("Name");
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals(1L, actualUser.getId().longValue());
        assertEquals("Name", actualUser.getName());
    }
}

