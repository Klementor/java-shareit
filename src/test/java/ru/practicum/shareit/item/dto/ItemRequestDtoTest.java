package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ItemRequestDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemRequestDto#ItemRequestDto()}
     *   <li>{@link ItemRequestDto#setAvailable(Boolean)}
     *   <li>{@link ItemRequestDto#setDescription(String)}
     *   <li>{@link ItemRequestDto#setId(Long)}
     *   <li>{@link ItemRequestDto#setName(String)}
     *   <li>{@link ItemRequestDto#setRequestId(Long)}
     *   <li>{@link ItemRequestDto#getAvailable()}
     *   <li>{@link ItemRequestDto#getDescription()}
     *   <li>{@link ItemRequestDto#getId()}
     *   <li>{@link ItemRequestDto#getName()}
     *   <li>{@link ItemRequestDto#getRequestId()}
     * </ul>
     */
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

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemRequestDto#ItemRequestDto(Long, String, String, Boolean, Long)}
     *   <li>{@link ItemRequestDto#setAvailable(Boolean)}
     *   <li>{@link ItemRequestDto#setDescription(String)}
     *   <li>{@link ItemRequestDto#setId(Long)}
     *   <li>{@link ItemRequestDto#setName(String)}
     *   <li>{@link ItemRequestDto#setRequestId(Long)}
     *   <li>{@link ItemRequestDto#getAvailable()}
     *   <li>{@link ItemRequestDto#getDescription()}
     *   <li>{@link ItemRequestDto#getId()}
     *   <li>{@link ItemRequestDto#getName()}
     *   <li>{@link ItemRequestDto#getRequestId()}
     * </ul>
     */
    @Test
    void testConstructor2() {
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

