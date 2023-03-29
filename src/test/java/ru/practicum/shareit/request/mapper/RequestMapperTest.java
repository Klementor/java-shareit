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
    /**
     * Method under test: {@link RequestMapper#toRequest(RequestDto, User)}
     */
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

    /**
     * Method under test: {@link RequestMapper#toResponse(Request, List)}
     */
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

    /**
     * Method under test: {@link RequestMapper#toResponse(Request, List)}
     */
    @Test
    void testToResponse2() {
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

    /**
     * Method under test: {@link RequestMapper#toResponse(Request, List)}
     */
    @Test
    void testToResponse3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user2);

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

    /**
     * Method under test: {@link RequestMapper#toResponse(Request, List)}
     */
    @Test
    void testToResponse4() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        COWArrayList<Item> itemList = (COWArrayList<Item>) mock(COWArrayList.class);
        when(itemList.iterator()).thenReturn(MappingIterator.emptyIterator());
        RequestResponseDto actualToResponseResult = RequestMapper.toResponse(request, itemList);
        assertTrue(actualToResponseResult.getItems().isEmpty());
        assertEquals("01:01", actualToResponseResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualToResponseResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualToResponseResult.getDescription());
        verify(itemList).iterator();
    }
}

