package ru.practicum.shareit.request.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

class RequestResponseDtoTest {

    @Test
    void testItemDtoToItemDtoFromItem() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);
        RequestResponseDto.ItemDto actualToItemDtoFromItemResult = RequestResponseDto.ItemDto.toItemDtoFromItem(item);
        assertTrue(actualToItemDtoFromItemResult.getAvailable());
        assertEquals(1L, actualToItemDtoFromItemResult.getRequestId().longValue());
        assertEquals("Name", actualToItemDtoFromItemResult.getName());
        assertEquals(1L, actualToItemDtoFromItemResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToItemDtoFromItemResult.getDescription());
    }
}

