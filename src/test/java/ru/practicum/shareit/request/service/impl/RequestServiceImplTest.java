package ru.practicum.shareit.request.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RequestServiceImpl.class})
@ExtendWith(SpringExtension.class)
class RequestServiceImplTest {
    @MockBean
    private ItemJpaRepository itemJpaRepository;

    @MockBean
    private ItemRequestJpaRepository itemRequestJpaRepository;

    @Autowired
    private RequestServiceImpl requestServiceImpl;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @Test
    void testPostNewItemRequest() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        Request request = createRequest(user1);
        when(itemRequestJpaRepository.save((Request) any())).thenReturn(request);

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");
        RequestResponseDto actualPostNewItemRequestResult = requestServiceImpl.postNewItemRequest(requestDto, 1L);
        assertEquals("01:01", actualPostNewItemRequestResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualPostNewItemRequestResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualPostNewItemRequestResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemRequestJpaRepository).save((Request) any());
    }

    @Test
    void testPostNewItemRequestSecond() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemRequestJpaRepository.save((Request) any())).thenThrow(new NotFoundException("An error occurred"));

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");
        assertThrows(NotFoundException.class, () -> requestServiceImpl.postNewItemRequest(requestDto, 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemRequestJpaRepository).save((Request) any());
    }

    @Test
    void testPostNewItemRequestThird() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user1 = createUser();

        Request request = createRequest(user1);
        when(itemRequestJpaRepository.save((Request) any())).thenReturn(request);

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");
        assertThrows(NotFoundException.class, () -> requestServiceImpl.postNewItemRequest(requestDto, 1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testPostNewItemRequestFourth() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();
        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);
        when(itemRequestJpaRepository.save((Request) any())).thenReturn(request);

        RequestDto requestDto = new RequestDto();
        requestDto.setDescription("The characteristics of someone or something");
        RequestResponseDto actualPostNewItemRequestResult = requestServiceImpl.postNewItemRequest(requestDto, 1L);
        assertEquals("01:01", actualPostNewItemRequestResult.getCreated().toLocalTime().toString());
        assertEquals(1L, actualPostNewItemRequestResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualPostNewItemRequestResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemRequestJpaRepository).save((Request) any());
        verify(request).getId();
        verify(request).getDescription();
        verify(request).getDateTimeOfCreate();
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetItemRequestByRequesterId() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(new ArrayList<>());
        assertTrue(requestServiceImpl.getItemRequestByRequesterId(1L).isEmpty());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
    }

    @Test
    void testGetItemRequestByRequesterIdSecond() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any()))
                .thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getItemRequestByRequesterId(1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
    }

    @Test
    void testGetItemRequestByRequesterIdThird() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(new ArrayList<>());
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getItemRequestByRequesterId(1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testGetItemRequestByRequesterIdFourth() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = createRequest(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(requestList);
        List<RequestResponseDto> actualItemRequestByRequesterId = requestServiceImpl.getItemRequestByRequesterId(1L);
        assertEquals(1, actualItemRequestByRequesterId.size());
        RequestResponseDto getResult = actualItemRequestByRequesterId.get(0);
        assertEquals(itemList, getResult.getItems());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
    }

    @Test
    void testGetItemRequestByRequesterIdFifth() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("dateTimeOfCreate");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user2 = createUser();

        Request request1 = createRequest(user2);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request1);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(requestList);
        List<RequestResponseDto> actualItemRequestByRequesterId = requestServiceImpl.getItemRequestByRequesterId(1L);
        assertEquals(1, actualItemRequestByRequesterId.size());
        RequestResponseDto getResult = actualItemRequestByRequesterId.get(0);
        List<RequestResponseDto.ItemDto> items = getResult.getItems();
        assertEquals(1, items.size());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getId().longValue());
        RequestResponseDto.ItemDto getResult1 = items.get(0);
        assertTrue(getResult1.getAvailable());
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("dateTimeOfCreate", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
    }

    /**
     * Method under test: {@link RequestServiceImpl#getItemRequestByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestByRequesterIdSixth() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = createRequest(user);

        User user1 = createUser();

        Request request1 = createRequest(user1);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request1);
        requestList.add(request);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(requestList);
        List<RequestResponseDto> actualItemRequestByRequesterId = requestServiceImpl.getItemRequestByRequesterId(1L);
        assertEquals(1, actualItemRequestByRequesterId.size());
        RequestResponseDto getResult = actualItemRequestByRequesterId.get(0);
        assertEquals(itemList, getResult.getItems());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository, atLeast(1)).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
    }


    @Test
    void testGetItemRequestByRequesterIdSeventh() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findAllByRequesterId((Long) any(), (Sort) any())).thenReturn(requestList);
        List<RequestResponseDto> actualItemRequestByRequesterId = requestServiceImpl.getItemRequestByRequesterId(1L);
        assertEquals(1, actualItemRequestByRequesterId.size());
        RequestResponseDto getResult = actualItemRequestByRequesterId.get(0);
        assertEquals(itemList, getResult.getItems());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findAllByRequesterId((Long) any(), (Sort) any());
        verify(request, atLeast(1)).getId();
        verify(request).getDescription();
        verify(request).getDateTimeOfCreate();
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetAllItemRequests() {
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(new ArrayList<>());
        assertTrue(requestServiceImpl.getAllItemRequests(1, 3, 1L).isEmpty());
        verify(itemRequestJpaRepository).findOtherUserItems((Long) any(), (Pageable) any());
    }

    @Test
    void testGetAllItemRequestsSecond() {
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = createRequest(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(requestList);
        List<RequestResponseDto> actualAllItemRequests = requestServiceImpl.getAllItemRequests(1, 3, 1L);
        assertEquals(1, actualAllItemRequests.size());
        RequestResponseDto getResult = actualAllItemRequests.get(0);
        assertEquals(itemList, getResult.getItems());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findOtherUserItems((Long) any(), (Pageable) any());
    }

    @Test
    void testGetAllItemRequestsThird() {
        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("dateTimeOfCreate");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user2 = createUser();

        Request request1 = createRequest(user2);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request1);
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(requestList);
        List<RequestResponseDto> actualAllItemRequests = requestServiceImpl.getAllItemRequests(1, 3, 1L);
        assertEquals(1, actualAllItemRequests.size());
        RequestResponseDto getResult = actualAllItemRequests.get(0);
        List<RequestResponseDto.ItemDto> items = getResult.getItems();
        assertEquals(1, items.size());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getId().longValue());
        RequestResponseDto.ItemDto getResult1 = items.get(0);
        assertTrue(getResult1.getAvailable());
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("dateTimeOfCreate", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findOtherUserItems((Long) any(), (Pageable) any());
    }

    @Test
    void testGetAllItemRequestsFourth() {
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(requestList);
        List<RequestResponseDto> actualAllItemRequests = requestServiceImpl.getAllItemRequests(1, 3, 1L);
        assertEquals(1, actualAllItemRequests.size());
        RequestResponseDto getResult = actualAllItemRequests.get(0);
        assertEquals(itemList, getResult.getItems());
        assertEquals("01:01", getResult.getCreated().toLocalTime().toString());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).findOtherUserItems((Long) any(), (Pageable) any());
        verify(request, atLeast(1)).getId();
        verify(request).getDescription();
        verify(request).getDateTimeOfCreate();
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetAllItemRequestsArithmeticException() {
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(requestList);
        assertThrows(ArithmeticException.class, () -> requestServiceImpl.getAllItemRequests(1, 0, 1L));
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetAllItemRequestsBadRequestException() {
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);

        ArrayList<Request> requestList = new ArrayList<>();
        requestList.add(request);
        when(itemRequestJpaRepository.findOtherUserItems((Long) any(), (Pageable) any())).thenReturn(requestList);
        assertThrows(BadRequestException.class, () -> requestServiceImpl.getAllItemRequests(1, 3, null));
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetItemRequestById() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = createRequest(user);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        RequestResponseDto actualItemRequestById = requestServiceImpl.getItemRequestById(1L, 1L);
        assertEquals(itemList, actualItemRequestById.getItems());
        assertEquals("01:01", actualItemRequestById.getCreated().toLocalTime().toString());
        assertEquals(1L, actualItemRequestById.getId().longValue());
        assertEquals("The characteristics of someone or something", actualItemRequestById.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void testGetItemRequestByIdSecond() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());
        when(itemRequestJpaRepository.getReferenceById((Long) any()))
                .thenThrow(new NotFoundException("An error occurred"));
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getItemRequestById(1L, 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void testGetItemRequestByIdThird() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        Request request = createRequest(user);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getItemRequestById(1L, 1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testGetItemRequestByIdFourth() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user2 = createUser();

        Request request1 = createRequest(user2);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request1);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        RequestResponseDto actualItemRequestById = requestServiceImpl.getItemRequestById(1L, 1L);
        List<RequestResponseDto.ItemDto> items = actualItemRequestById.getItems();
        assertEquals(1, items.size());
        assertEquals("01:01", actualItemRequestById.getCreated().toLocalTime().toString());
        assertEquals("The characteristics of someone or something", actualItemRequestById.getDescription());
        assertEquals(1L, actualItemRequestById.getId().longValue());
        RequestResponseDto.ItemDto getResult = items.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals(1L, getResult.getRequestId().longValue());
        assertEquals("Name", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void testGetItemRequestByIdFifth() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(itemList);

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        RequestResponseDto actualItemRequestById = requestServiceImpl.getItemRequestById(1L, 1L);
        assertEquals(itemList, actualItemRequestById.getItems());
        assertEquals("01:01", actualItemRequestById.getCreated().toLocalTime().toString());
        assertEquals(1L, actualItemRequestById.getId().longValue());
        assertEquals("The characteristics of someone or something", actualItemRequestById.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByRequestId((Long) any());
        verify(itemRequestJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).getReferenceById((Long) any());
        verify(request).getId();
        verify(request).getDescription();
        verify(request).getDateTimeOfCreate();
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetItemRequestByIdNotFoundException() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> requestServiceImpl.getItemRequestById(1L, 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemRequestJpaRepository).existsById((Long) any());
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
    }

    @Test
    void testGetItemRequestByIdBadRequestException() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByRequestId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();
        Request request = mock(Request.class);
        when(request.getId()).thenReturn(1L);
        when(request.getDescription()).thenReturn("The characteristics of someone or something");
        when(request.getDateTimeOfCreate()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(request).setDateTimeOfCreate((LocalDateTime) any());
        doNothing().when(request).setDescription((String) any());
        doNothing().when(request).setId((Long) any());
        doNothing().when(request).setRequester((User) any());
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        when(itemRequestJpaRepository.getReferenceById((Long) any())).thenReturn(request);
        when(itemRequestJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> requestServiceImpl.getItemRequestById(1L, null));
        verify(request).setDateTimeOfCreate((LocalDateTime) any());
        verify(request).setDescription((String) any());
        verify(request).setId((Long) any());
        verify(request).setRequester((User) any());
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

