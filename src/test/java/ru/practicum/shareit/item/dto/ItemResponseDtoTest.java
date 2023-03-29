package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ItemResponseDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemResponseDto#ItemResponseDto()}
     *   <li>{@link ItemResponseDto#setAvailable(Boolean)}
     *   <li>{@link ItemResponseDto#setDescription(String)}
     *   <li>{@link ItemResponseDto#setId(Long)}
     *   <li>{@link ItemResponseDto#setName(String)}
     *   <li>{@link ItemResponseDto#setRequestId(Long)}
     *   <li>{@link ItemResponseDto#getAvailable()}
     *   <li>{@link ItemResponseDto#getDescription()}
     *   <li>{@link ItemResponseDto#getId()}
     *   <li>{@link ItemResponseDto#getName()}
     *   <li>{@link ItemResponseDto#getRequestId()}
     * </ul>
     */
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

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemResponseDto#ItemResponseDto(Long, String, String, Boolean, Long)}
     *   <li>{@link ItemResponseDto#setAvailable(Boolean)}
     *   <li>{@link ItemResponseDto#setDescription(String)}
     *   <li>{@link ItemResponseDto#setId(Long)}
     *   <li>{@link ItemResponseDto#setName(String)}
     *   <li>{@link ItemResponseDto#setRequestId(Long)}
     *   <li>{@link ItemResponseDto#getAvailable()}
     *   <li>{@link ItemResponseDto#getDescription()}
     *   <li>{@link ItemResponseDto#getId()}
     *   <li>{@link ItemResponseDto#getName()}
     *   <li>{@link ItemResponseDto#getRequestId()}
     * </ul>
     */
    @Test
    void testConstructor2() {
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

