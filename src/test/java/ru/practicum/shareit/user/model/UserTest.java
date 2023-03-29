package ru.practicum.shareit.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link User#User()}
     *   <li>{@link User#setEmail(String)}
     *   <li>{@link User#setId(Long)}
     *   <li>{@link User#setName(String)}
     *   <li>{@link User#getEmail()}
     *   <li>{@link User#getId()}
     *   <li>{@link User#getName()}
     * </ul>
     */
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

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link User#User(Long, String, String)}
     *   <li>{@link User#setEmail(String)}
     *   <li>{@link User#setId(Long)}
     *   <li>{@link User#setName(String)}
     *   <li>{@link User#getEmail()}
     *   <li>{@link User#getId()}
     *   <li>{@link User#getName()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        User actualUser = new User(1L, "Name", "jane.doe@example.org");
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setId(1L);
        actualUser.setName("Name");
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals(1L, actualUser.getId().longValue());
        assertEquals("Name", actualUser.getName());
    }
}

