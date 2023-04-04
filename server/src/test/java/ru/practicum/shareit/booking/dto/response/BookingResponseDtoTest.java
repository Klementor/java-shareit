package ru.practicum.shareit.booking.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

class BookingResponseDtoTest {

    @Test
    void testItemDtoFromItem() {
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
        BookingResponseDto.ItemDto actualFromItemResult = BookingResponseDto.ItemDto.fromItem(item);
        assertEquals(1L, actualFromItemResult.getId().longValue());
        assertEquals("Name", actualFromItemResult.getName());
    }

    @Test
    void testUserDtoFromUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        assertEquals(1L, BookingResponseDto.UserDto.fromUser(user).getId().longValue());
    }
}

