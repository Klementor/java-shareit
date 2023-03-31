package ru.practicum.shareit.request.mapper;

import ch.qos.logback.core.util.COWArrayList;
import com.fasterxml.jackson.databind.MappingIterator;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestMapperTest {

    @Test
    void testToRequest() {
        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Request actualToRequestResult = RequestMapper.toRequest(requestDto, user);
        assertSame(user, actualToRequestResult.getRequester());
        assertEquals("The characteristics of someone or something", actualToRequestResult.getDescription());
    }

    @Test
    void testToResponse() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        ArrayList<Item> itemList = new ArrayList<>();
        RequestResponseDto actualToResponseResult = RequestMapper.toResponse(request, itemList);
        assertEquals(itemList, actualToResponseResult.getItems());
        assertEquals("01:01", actualToResponseResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
    }

    @Test
    void testToResponseSecond() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        RequestResponseDto actualToResponseResult = RequestMapper.toResponse(request, null);
        assertEquals("01:01", actualToResponseResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
    }

    @Test
    void testToResponseThird() {
        User user = createUser();

        Request request = createRequest(user);

        User user1 = createUser();

        User user2 = createUser();

        Request request1 = createRequest(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        RequestResponseDto actualToResponseResult = RequestMapper.toResponse(request, itemList);
        List<RequestResponseDto.ItemDto> items = actualToResponseResult.getItems();
        assertEquals(1, items.size());
        assertEquals("01:01", actualToResponseResult.getCreated().toLocalTime().toString());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        RequestResponseDto.ItemDto getResult = items.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals(1L, getResult.getRequestId().longValue());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
    }


    @Test
    void testToResponseFourth() {
        User user = createUser();

        Request request = createRequest(user);

        COWArrayList<Item> itemList = (COWArrayList<Item>) mock(COWArrayList.class);
        when(itemList.iterator()).thenReturn(MappingIterator.emptyIterator());
        RequestResponseDto actualToResponseResult = RequestMapper.toResponse(request, itemList);
        assertTrue(actualToResponseResult.getItems().isEmpty());
        assertEquals("01:01", actualToResponseResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        verify(itemList).iterator();
    }

    private User createUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        return user;
    }

    private Request createRequest(User user) {
        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        return request;
    }
}

