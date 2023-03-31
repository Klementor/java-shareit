package ru.practicum.shareit.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserResponseDtoTest {

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

    @Test
    void testConstructorSecond() {
        UserResponseDto actualUserResponseDto = new UserResponseDto(1L, "Name", "jane.doe@example.org");
        actualUserResponseDto.setEmail("jane.doe@example.org");
        actualUserResponseDto.setId(1L);
        actualUserResponseDto.setName("Name");
        assertEquals("jane.doe@example.org", actualUserResponseDto.getEmail());
        assertEquals(1L, actualUserResponseDto.getId().longValue());
        assertEquals("Name", actualUserResponseDto.getName());
    }
}

