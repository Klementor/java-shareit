package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ItemRequestDtoTest {

    @Test
    void testConstructor() {
        ItemRequestDto actualItemRequestDto = new ItemRequestDto();
        actualItemRequestDto.setAvailable(true);
        actualItemRequestDto.setDescription("The characteristics of someone or something");
        actualItemRequestDto.setId(1L);
        actualItemRequestDto.setName("Name");
        actualItemRequestDto.setRequestId(1L);
        assertTrue(actualItemRequestDto.getAvailable());
        assertEquals("The characteristics of someone or something", actualItemRequestDto.getDescription());
        assertEquals(1L, actualItemRequestDto.getId().longValue());
        assertEquals("Name", actualItemRequestDto.getName());
        assertEquals(1L, actualItemRequestDto.getRequestId().longValue());
    }


    @Test
    void testConstructorSecond() {
        ItemRequestDto actualItemRequestDto = new ItemRequestDto(1L, "Name",
                "The characteristics of someone or something", true, 1L);
        actualItemRequestDto.setAvailable(true);
        actualItemRequestDto.setDescription("The characteristics of someone or something");
        actualItemRequestDto.setId(1L);
        actualItemRequestDto.setName("Name");
        actualItemRequestDto.setRequestId(1L);
        assertTrue(actualItemRequestDto.getAvailable());
        assertEquals("The characteristics of someone or something", actualItemRequestDto.getDescription());
        assertEquals(1L, actualItemRequestDto.getId().longValue());
        assertEquals("Name", actualItemRequestDto.getName());
        assertEquals(1L, actualItemRequestDto.getRequestId().longValue());
    }
}

