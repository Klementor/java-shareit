package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ItemResponseDtoTest {

    @Test
    void testConstructor() {
        ItemResponseDto actualItemResponseDto = new ItemResponseDto();
        actualItemResponseDto.setAvailable(true);
        actualItemResponseDto.setDescription("The characteristics of someone or something");
        actualItemResponseDto.setId(1L);
        actualItemResponseDto.setName("Name");
        actualItemResponseDto.setRequestId(1L);
        assertTrue(actualItemResponseDto.getAvailable());
        assertEquals("The characteristics of someone or something", actualItemResponseDto.getDescription());
        assertEquals(1L, actualItemResponseDto.getId().longValue());
        assertEquals("Name", actualItemResponseDto.getName());
        assertEquals(1L, actualItemResponseDto.getRequestId().longValue());
    }

    @Test
    void testConstructorSecond() {
        ItemResponseDto actualItemResponseDto = new ItemResponseDto(1L, "Name",
                "The characteristics of someone or something", true, 1L);
        actualItemResponseDto.setAvailable(true);
        actualItemResponseDto.setDescription("The characteristics of someone or something");
        actualItemResponseDto.setId(1L);
        actualItemResponseDto.setName("Name");
        actualItemResponseDto.setRequestId(1L);
        assertTrue(actualItemResponseDto.getAvailable());
        assertEquals("The characteristics of someone or something", actualItemResponseDto.getDescription());
        assertEquals(1L, actualItemResponseDto.getId().longValue());
        assertEquals("Name", actualItemResponseDto.getName());
        assertEquals(1L, actualItemResponseDto.getRequestId().longValue());
    }
}

