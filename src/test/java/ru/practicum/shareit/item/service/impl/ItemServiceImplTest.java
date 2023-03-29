package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.ItemRequestJpaRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ItemServiceImplTest {
    @MockBean
    private BookingJpaRepository bookingJpaRepository;

    @MockBean
    private CommentJpaRepository commentJpaRepository;

    @MockBean
    private ItemJpaRepository itemJpaRepository;

    @MockBean
    private ItemRequestJpaRepository itemRequestJpaRepository;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @MockBean
    private UserJpaRepository userJpaRepository;

    /**
     * Method under test: {@link ItemServiceImpl#addItem(ItemRequestDto, Long)}
     */
    @Test
    void testAddItem() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.save((Item) any())).thenReturn(item);
        ItemResponseDto actualAddItemResult = itemServiceImpl.addItem(new ItemRequestDto(), 1L);
        assertTrue(actualAddItemResult.getAvailable());
        assertEquals(1L, actualAddItemResult.getRequestId().longValue());
        assertEquals("Name", actualAddItemResult.getName());
        assertEquals(1L, actualAddItemResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualAddItemResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(ItemRequestDto, Long)}
     */
    @Test
    void testAddItem2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.save((Item) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.addItem(new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#addItem(ItemRequestDto, Long)}
     */
    @Test
    void testAddItem3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.save((Item) any())).thenReturn(item);
        assertThrows(UserNotFoundException.class, () -> itemServiceImpl.addItem(new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#updateItem(Long, ItemRequestDto, Long)}
     */
    @Test
    void testUpdateItem() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user3);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user2);
        item1.setRequest(request1);
        when(itemJpaRepository.save((Item) any())).thenReturn(item1);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        ItemResponseDto actualUpdateItemResult = itemServiceImpl.updateItem(1L, new ItemRequestDto(), 1L);
        assertTrue(actualUpdateItemResult.getAvailable());
        assertEquals(1L, actualUpdateItemResult.getRequestId().longValue());
        assertEquals("Name", actualUpdateItemResult.getName());
        assertEquals(1L, actualUpdateItemResult.getId().longValue());
        assertEquals("The characteristics of someone or something", actualUpdateItemResult.getDescription());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository, atLeast(1)).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#updateItem(Long, ItemRequestDto, Long)}
     */
    @Test
    void testUpdateItem2() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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
        when(itemJpaRepository.save((Item) any())).thenThrow(new NotFoundException("An error occurred"));
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> itemServiceImpl.updateItem(1L, new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository, atLeast(1)).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#updateItem(Long, ItemRequestDto, Long)}
     */
    @Test
    void testUpdateItem3() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

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

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user3);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user2);
        item1.setRequest(request1);
        when(itemJpaRepository.save((Item) any())).thenReturn(item1);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> itemServiceImpl.updateItem(1L, new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Booking booking1 = new Booking();
        booking1.setBooker(user3);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(1L);
        user7.setName("Name");

        Request request2 = new Request();
        request2.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);
        request2.setRequester(user7);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(user6);
        item2.setRequest(request2);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item2);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentJpaRepository.findAllByItemId((Long) any())).thenReturn(commentList);
        ItemWithBookingsResponseDto actualItemById = itemServiceImpl.getItemById(1L, 1L);
        assertTrue(actualItemById.getAvailable());
        assertEquals("Name", actualItemById.getName());
        assertEquals(commentList, actualItemById.getComments());
        assertEquals(1L, actualItemById.getId().longValue());
        assertEquals("The characteristics of someone or something", actualItemById.getDescription());
        ItemWithBookingsResponseDto.BookingDto lastBooking = actualItemById.getLastBooking();
        assertEquals(1L, lastBooking.getId().longValue());
        assertEquals(1L, lastBooking.getBookerId().longValue());
        ItemWithBookingsResponseDto.BookingDto nextBooking = actualItemById.getNextBooking();
        assertEquals(1L, nextBooking.getId().longValue());
        assertEquals(1L, nextBooking.getBookerId().longValue());
        verify(bookingJpaRepository).findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).getReferenceById((Long) any());
        verify(commentJpaRepository).findAllByItemId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Booking booking1 = new Booking();
        booking1.setBooker(user3);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(1L);
        user7.setName("Name");

        Request request2 = new Request();
        request2.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);
        request2.setRequester(user7);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(user6);
        item2.setRequest(request2);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item2);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        when(commentJpaRepository.findAllByItemId((Long) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.getItemById(1L, 1L));
        verify(bookingJpaRepository).findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).getReferenceById((Long) any());
        verify(commentJpaRepository).findAllByItemId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenReturn(1L);
        when(booking.getStatus()).thenReturn(Booking.Status.REJECTED);
        when(booking.getBooker()).thenReturn(user3);
        doNothing().when(booking).setBooker((User) any());
        doNothing().when(booking).setEnd((LocalDateTime) any());
        doNothing().when(booking).setId((Long) any());
        doNothing().when(booking).setItem((Item) any());
        doNothing().when(booking).setStart((LocalDateTime) any());
        doNothing().when(booking).setStatus((Booking.Status) any());
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult = Optional.of(booking);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user6);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user5);
        item1.setRequest(request1);

        Booking booking1 = new Booking();
        booking1.setBooker(user4);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(1L);
        user7.setName("Name");

        User user8 = new User();
        user8.setEmail("jane.doe@example.org");
        user8.setId(1L);
        user8.setName("Name");

        Request request2 = new Request();
        request2.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request2.setDescription("The characteristics of someone or something");
        request2.setId(1L);
        request2.setRequester(user8);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setDescription("The characteristics of someone or something");
        item2.setId(1L);
        item2.setName("Name");
        item2.setOwner(user7);
        item2.setRequest(request2);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item2);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        ArrayList<Comment> commentList = new ArrayList<>();
        when(commentJpaRepository.findAllByItemId((Long) any())).thenReturn(commentList);
        ItemWithBookingsResponseDto actualItemById = itemServiceImpl.getItemById(1L, 1L);
        assertTrue(actualItemById.getAvailable());
        assertEquals("Name", actualItemById.getName());
        assertEquals(commentList, actualItemById.getComments());
        assertEquals(1L, actualItemById.getId().longValue());
        assertEquals("The characteristics of someone or something", actualItemById.getDescription());
        ItemWithBookingsResponseDto.BookingDto nextBooking = actualItemById.getNextBooking();
        assertEquals(1L, nextBooking.getId().longValue());
        assertEquals(1L, nextBooking.getBookerId().longValue());
        verify(bookingJpaRepository).findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any());
        verify(booking).getStatus();
        verify(booking).setBooker((User) any());
        verify(booking).setEnd((LocalDateTime) any());
        verify(booking).setId((Long) any());
        verify(booking).setItem((Item) any());
        verify(booking).setStart((LocalDateTime) any());
        verify(booking).setStatus((Booking.Status) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).getReferenceById((Long) any());
        verify(commentJpaRepository).findAllByItemId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems2() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any()))
                .thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.getUserItems(1L));
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems4() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(2L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("john.smith@example.org");
        user4.setId(2L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("john.smith@example.org");
        user5.setId(2L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("Получение всех предметов пользователя с id = {}");
        request1.setId(2L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(false);
        item1.setDescription("Получение всех предметов пользователя с id = {}");
        item1.setId(2L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Booking booking1 = new Booking();
        booking1.setBooker(user3);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(2L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.APPROVED);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems5() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems6() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(2L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("john.smith@example.org");
        user4.setId(2L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("john.smith@example.org");
        user5.setId(2L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("Получение всех предметов пользователя с id = {}");
        request1.setId(2L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(false);
        item1.setDescription("Получение всех предметов пользователя с id = {}");
        item1.setId(2L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Booking booking1 = new Booking();
        booking1.setBooker(user3);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(2L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.APPROVED);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems7() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertThrows(UserNotFoundException.class, () -> itemServiceImpl.getUserItems(1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems8() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertEquals(1, itemServiceImpl.getUserItems(1L).size());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems9() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setId(1L);
        comment.setItem(item);
        comment.setText("Получение всех предметов пользователя с id = {}");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(commentList);
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems10() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setId(1L);
        comment.setItem(item);
        comment.setText("Получение всех предметов пользователя с id = {}");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(2L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("john.smith@example.org");
        user4.setId(2L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("john.smith@example.org");
        user5.setId(2L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("Получение всех предметов пользователя с id = {}");
        request1.setId(2L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(false);
        item1.setDescription("Получение всех предметов пользователя с id = {}");
        item1.setId(2L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Comment comment1 = new Comment();
        comment1.setAuthor(user3);
        comment1.setId(2L);
        comment1.setItem(item1);
        comment1.setText("Text");
        comment1.setTime(LocalDateTime.of(1, 1, 1, 1, 1));

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment);
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(commentList);
        assertTrue(itemServiceImpl.getUserItems(1L).isEmpty());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#getUserItems(Long)}
     */
    @Test
    void testGetUserItems13() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Получение всех предметов пользователя с id = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Получение всех предметов пользователя с id = {}");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Получение всех предметов пользователя с id = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Получение всех предметов пользователя с id = {}");
        item.setOwner(user1);
        item.setRequest(request);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Получение всех предметов пользователя с id = {}");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Получение всех предметов пользователя с id = {}");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user4);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Получение всех предметов пользователя с id = {}");
        item1.setOwner(user3);
        item1.setRequest(request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(itemList);
        when(commentJpaRepository.findAllByItemIdIn((List<Long>) any())).thenReturn(new ArrayList<>());
        assertEquals(1, itemServiceImpl.getUserItems(1L).size());
        verify(bookingJpaRepository).findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any());
        verify(bookingJpaRepository).findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).findAllByOwnerId((Long) any());
        verify(commentJpaRepository).findAllByItemIdIn((List<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByText(String, Long)}
     */
    @Test
    void testSearchItemsByText() {
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByText("Text", 1L).isEmpty());
        verify(itemJpaRepository).findAllByText((String) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByText(String, Long)}
     */
    @Test
    void testSearchItemsByText2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Найдены все предметы с подстрокой = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Найдены все предметы с подстрокой = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Найдены все предметы с подстрокой = {}");
        item.setOwner(user);
        item.setRequest(request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(itemList);
        List<ItemResponseDto> actualSearchItemsByTextResult = itemServiceImpl.searchItemsByText("Text", 1L);
        assertEquals(1, actualSearchItemsByTextResult.size());
        ItemResponseDto getResult = actualSearchItemsByTextResult.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals(1L, getResult.getRequestId().longValue());
        assertEquals("Найдены все предметы с подстрокой = {}", getResult.getName());
        assertEquals(1L, getResult.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        verify(itemJpaRepository).findAllByText((String) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByText(String, Long)}
     */
    @Test
    void testSearchItemsByText3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Найдены все предметы с подстрокой = {}");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Найдены все предметы с подстрокой = {}");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user1);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Найдены все предметы с подстрокой = {}");
        item.setOwner(user);
        item.setRequest(request);

        User user2 = new User();
        user2.setEmail("john.smith@example.org");
        user2.setId(2L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("john.smith@example.org");
        user3.setId(2L);
        user3.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("Найдены все предметы с подстрокой = {}");
        request1.setId(2L);
        request1.setRequester(user3);

        Item item1 = new Item();
        item1.setAvailable(false);
        item1.setDescription("Найдены все предметы с подстрокой = {}");
        item1.setId(2L);
        item1.setName("Name");
        item1.setOwner(user2);
        item1.setRequest(request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(itemList);
        List<ItemResponseDto> actualSearchItemsByTextResult = itemServiceImpl.searchItemsByText("Text", 1L);
        assertEquals(2, actualSearchItemsByTextResult.size());
        ItemResponseDto getResult = actualSearchItemsByTextResult.get(0);
        assertEquals(2L, getResult.getRequestId().longValue());
        ItemResponseDto getResult1 = actualSearchItemsByTextResult.get(1);
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("Найдены все предметы с подстрокой = {}", getResult1.getName());
        assertEquals(1L, getResult1.getId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        assertTrue(getResult1.getAvailable());
        assertEquals("Name", getResult.getName());
        assertEquals(2L, getResult.getId().longValue());
        assertEquals("Найдены все предметы с подстрокой = {}", getResult.getDescription());
        assertFalse(getResult.getAvailable());
        verify(itemJpaRepository).findAllByText((String) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByText(String, Long)}
     */
    @Test
    void testSearchItemsByText4() {
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByText("", 1L).isEmpty());
    }

    /**
     * Method under test: {@link ItemServiceImpl#searchItemsByText(String, Long)}
     */
    @Test
    void testSearchItemsByText5() {
        when(itemJpaRepository.findAllByText((String) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.searchItemsByText("Text", 1L));
        verify(itemJpaRepository).findAllByText((String) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#postComment(Long, Long, CommentRequestDto)}
     */
    @Test
    void testPostComment() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Comment comment = new Comment();
        comment.setAuthor(user3);
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        CommentResponseDto actualPostCommentResult = itemServiceImpl.postComment(1L, 1L, commentRequestDto);
        assertEquals("Name", actualPostCommentResult.getAuthorName());
        assertEquals("Text", actualPostCommentResult.getText());
        assertEquals(1L, actualPostCommentResult.getId().longValue());
        assertEquals("0001-01-01", actualPostCommentResult.getCreated().toLocalDate().toString());
        verify(bookingJpaRepository).existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).getReferenceById((Long) any());
        verify(commentJpaRepository).save((Comment) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#postComment(Long, Long, CommentRequestDto)}
     */
    @Test
    void testPostComment2() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        when(commentJpaRepository.save((Comment) any())).thenThrow(new NotFoundException("An error occurred"));

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(NotFoundException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(bookingJpaRepository).existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).getReferenceById((Long) any());
        verify(commentJpaRepository).save((Comment) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#postComment(Long, Long, CommentRequestDto)}
     */
    @Test
    void testPostComment3() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(false);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Comment comment = new Comment();
        comment.setAuthor(user3);
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(BadRequestException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(bookingJpaRepository).existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#postComment(Long, Long, CommentRequestDto)}
     */
    @Test
    void testPostComment4() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Comment comment = new Comment();
        comment.setAuthor(user3);
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(NotFoundException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImpl#postComment(Long, Long, CommentRequestDto)}
     */
    @Test
    void testPostComment5() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user2);

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user1);
        item.setRequest(request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(false);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user5);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setDescription("The characteristics of someone or something");
        item1.setId(1L);
        item1.setName("Name");
        item1.setOwner(user4);
        item1.setRequest(request1);

        Comment comment = new Comment();
        comment.setAuthor(user3);
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(NotFoundException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(itemJpaRepository).existsById((Long) any());
    }
}