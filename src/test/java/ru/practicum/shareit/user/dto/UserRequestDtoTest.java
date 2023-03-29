package ru.practicum.shareit.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserRequestDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserRequestDto#UserRequestDto()}
     *   <li>{@link UserRequestDto#setEmail(String)}
     *   <li>{@link UserRequestDto#setName(String)}
     *   <li>{@link UserRequestDto#getEmail()}
     *   <li>{@link UserRequestDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UserRequestDto actualUserRequestDto = new UserRequestDto();
        actualUserRequestDto.setEmail("jane.doe@example.org");
        actualUserRequestDto.setName("Name");
        assertEquals("jane.doe@example.org", actualUserRequestDto.getEmail());
        assertEquals("Name", actualUserRequestDto.getName());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserRequestDto#UserRequestDto(String, String)}
     *   <li>{@link UserRequestDto#setEmail(String)}
     *   <li>{@link UserRequestDto#setName(String)}
     *   <li>{@link UserRequestDto#getEmail()}
     *   <li>{@link UserRequestDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        UserRequestDto actualUserRequestDto = new UserRequestDto("Name", "jane.doe@example.org");
        actualUserRequestDto.setEmail("jane.doe@example.org");
        actualUserRequestDto.setName("Name");
        assertEquals("jane.doe@example.org", actualUserRequestDto.getEmail());
        assertEquals("Name", actualUserRequestDto.getName());
    }
}

