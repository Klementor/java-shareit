package ru.practicum.shareit.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserResponseDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserResponseDto#UserResponseDto()}
     *   <li>{@link UserResponseDto#setEmail(String)}
     *   <li>{@link UserResponseDto#setId(Long)}
     *   <li>{@link UserResponseDto#setName(String)}
     *   <li>{@link UserResponseDto#getEmail()}
     *   <li>{@link UserResponseDto#getId()}
     *   <li>{@link UserResponseDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UserResponseDto actualUserResponseDto = new UserResponseDto();
        actualUserResponseDto.setEmail("jane.doe@example.org");
        actualUserResponseDto.setId(1L);
        actualUserResponseDto.setName("Name");
        assertEquals("jane.doe@example.org", actualUserResponseDto.getEmail());
        assertEquals(1L, actualUserResponseDto.getId().longValue());
        assertEquals("Name", actualUserResponseDto.getName());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserResponseDto#UserResponseDto(Long, String, String)}
     *   <li>{@link UserResponseDto#setEmail(String)}
     *   <li>{@link UserResponseDto#setId(Long)}
     *   <li>{@link UserResponseDto#setName(String)}
     *   <li>{@link UserResponseDto#getEmail()}
     *   <li>{@link UserResponseDto#getId()}
     *   <li>{@link UserResponseDto#getName()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        UserResponseDto actualUserResponseDto = new UserResponseDto(1L, "Name", "jane.doe@example.org");
        actualUserResponseDto.setEmail("jane.doe@example.org");
        actualUserResponseDto.setId(1L);
        actualUserResponseDto.setName("Name");
        assertEquals("jane.doe@example.org", actualUserResponseDto.getEmail());
        assertEquals(1L, actualUserResponseDto.getId().longValue());
        assertEquals("Name", actualUserResponseDto.getName());
    }
}

