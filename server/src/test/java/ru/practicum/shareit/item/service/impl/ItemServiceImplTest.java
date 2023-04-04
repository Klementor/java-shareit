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

    @Test
    void testAddItem() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
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

    @Test
    void testAddItemSecond() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.save((Item) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.addItem(new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    @Test
    void testAddItemThird() {
        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
        when(itemJpaRepository.save((Item) any())).thenReturn(item);
        assertThrows(UserNotFoundException.class, () -> itemServiceImpl.addItem(new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
    }

    @Test
    void testUpdateItem() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user2 = createUser();

        User user3 = createUser();

        Request request1 = new Request();
        request1.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request1.setDescription("The characteristics of someone or something");
        request1.setId(1L);
        request1.setRequester(user3);

        Item item1 = createItem(user2, request1);
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

    @Test
    void testUpdateItemSecond() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);
        when(itemJpaRepository.save((Item) any())).thenThrow(new NotFoundException("An error occurred"));
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> itemServiceImpl.updateItem(1L, new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
        verify(itemJpaRepository, atLeast(1)).getReferenceById((Long) any());
        verify(itemJpaRepository).save((Item) any());
    }

    @Test
    void testUpdateItemThird() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user2 = createUser();

        User user3 = createUser();

        Request request1 = createRequest(user3);

        Item item1 = createItem(user2, request1);
        when(itemJpaRepository.save((Item) any())).thenReturn(item1);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> itemServiceImpl.updateItem(1L, new ItemRequestDto(), 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    @Test
    void testGetItemById() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);
        Optional<Booking> ofResult = Optional.of(booking);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Booking booking1 = createBooking(user3, item1);

        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user6 = createUser();

        User user7 = createUser();

        Request request2 = createRequest(user7);

        Item item2 = createItem(user6, request2);
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

    @Test
    void testGetItemByIdSecond() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);
        Optional<Booking> ofResult = Optional.of(booking);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Booking booking1 = createBooking(user3, item1);
        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user6 = createUser();

        User user7 = createUser();

        Request request2 = createRequest(user7);

        Item item2 = createItem(user6, request2);
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

    @Test
    void testGetItemByIdThird() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

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

        User user4 = createUser();

        User user5 = createUser();

        User user6 = createUser();

        Request request1 = createRequest(user6);

        Item item1 = createItem(user5, request1);

        Booking booking1 = createBooking(user4, item1);

        Optional<Booking> ofResult1 = Optional.of(booking1);
        when(bookingJpaRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult);
        when(bookingJpaRepository.findFirstByItemIdAndStartIsAfterOrderByStart((Long) any(), (LocalDateTime) any()))
                .thenReturn(ofResult1);

        User user7 = createUser();

        User user8 = createUser();

        Request request2 = createRequest(user8);

        Item item2 = createItem(user7, request2);
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

    @Test
    void testGetUserItemsSecond() {
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

    @Test
    void testGetUserItemsThird() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);

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

    @Test
    void testGetUserItemsFourth() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Booking booking1 = createBooking(user3, item1);
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
    }

    @Test
    void testGetUserItemsFifth() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);

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

    @Test
    void testGetUserItemsSixth() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Booking booking1 = createBooking(user3, item1);

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
    }

    @Test
    void testGetUserItemsSeventh() {
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

    @Test
    void testGetUserItemsEighth() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);

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

    @Test
    void testGetUserItemsNinth() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Comment comment = createComment(user, item);

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

    @Test
    void testGetUserItemsTenth() {
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(itemJpaRepository.findAllByOwnerId((Long) any())).thenReturn(new ArrayList<>());

        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Comment comment = createComment(user, item);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Comment comment1 = createComment(user3, item1);

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

    @Test
    void testGetUserItem() {
        User user = createUser();

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);

        Booking booking = createBooking(user, item);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(bookingJpaRepository.findFirstByItemIdInAndEndIsBeforeOrderByEndDesc((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(bookingList);
        when(bookingJpaRepository.findFirstByItemIdInAndStartIsAfterOrderByStart((List<Long>) any(),
                (LocalDateTime) any())).thenReturn(new ArrayList<>());
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = createUser();

        User user4 = createUser();

        Request request1 = createRequest(user4);

        Item item1 = createItem(user3, request1);

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

    @Test
    void testSearchItemsByText() {
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByText("Text", 1L).isEmpty());
        verify(itemJpaRepository).findAllByText((String) any());
    }

    @Test
    void testSearchItemsByTextSecond() {
        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(itemList);
        List<ItemResponseDto> actualSearchItemsByTextResult = itemServiceImpl.searchItemsByText("Text", 1L);
        assertEquals(1, actualSearchItemsByTextResult.size());
        ItemResponseDto getResult = actualSearchItemsByTextResult.get(0);
        assertTrue(getResult.getAvailable());
        assertEquals(1L, getResult.getRequestId().longValue());
    }

    @Test
    void testSearchItemsByTextThird() {
        User user = createUser();

        User user1 = createUser();

        Request request = createRequest(user1);

        Item item = createItem(user, request);

        User user2 = createUser();

        User user3 = createUser();

        Request request1 = createRequest(user3);

        Item item1 = createItem(user2, request1);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item);
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(itemList);
        List<ItemResponseDto> actualSearchItemsByTextResult = itemServiceImpl.searchItemsByText("Text", 1L);
        assertEquals(2, actualSearchItemsByTextResult.size());
        ItemResponseDto getResult = actualSearchItemsByTextResult.get(0);
        assertEquals(1L, getResult.getRequestId().longValue());
        ItemResponseDto getResult1 = actualSearchItemsByTextResult.get(1);
        assertEquals(1L, getResult1.getRequestId().longValue());
        assertEquals("The characteristics of someone or something", getResult1.getDescription());
        assertTrue(getResult1.getAvailable());
        assertEquals("Name", getResult.getName());
    }

    @Test
    void testSearchItemsByTextFourth() {
        when(itemJpaRepository.findAllByText((String) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.searchItemsByText("", 1L).isEmpty());
    }

    @Test
    void testSearchItemsByTextFifth() {
        when(itemJpaRepository.findAllByText((String) any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> itemServiceImpl.searchItemsByText("Text", 1L));
        verify(itemJpaRepository).findAllByText((String) any());
    }

    @Test
    void testPostComment() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Comment comment = createComment(user3, item1);
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

    @Test
    void testPostCommentSecond() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
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

    @Test
    void testPostCommentThird() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(false);

        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Comment comment = createComment(user3, item1);
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(BadRequestException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(bookingJpaRepository).existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    @Test
    void testPostCommentFourth() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(true);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Comment comment = createComment(user3, item1);
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(NotFoundException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(userJpaRepository).existsById((Long) any());
        verify(itemJpaRepository).existsById((Long) any());
    }

    @Test
    void testPostCommentFifth() {
        when(bookingJpaRepository.existsByBookerIdAndItemIdAndEndIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        User user = createUser();
        when(userJpaRepository.getReferenceById((Long) any())).thenReturn(user);
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

        User user1 = createUser();

        User user2 = createUser();

        Request request = createRequest(user2);

        Item item = createItem(user1, request);
        when(itemJpaRepository.getReferenceById((Long) any())).thenReturn(item);
        when(itemJpaRepository.existsById((Long) any())).thenReturn(false);

        User user3 = createUser();

        User user4 = createUser();

        User user5 = createUser();

        Request request1 = createRequest(user5);

        Item item1 = createItem(user4, request1);

        Comment comment = createComment(user3, item1);
        when(commentJpaRepository.save((Comment) any())).thenReturn(comment);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Text");
        assertThrows(NotFoundException.class, () -> itemServiceImpl.postComment(1L, 1L, commentRequestDto));
        verify(itemJpaRepository).existsById((Long) any());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        return user;
    }

    private Item createItem(User user, Request request) {
        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("The characteristics of someone or something");
        item.setId(1L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequest(request);
        return item;
    }

    private Request createRequest(User user) {
        Request request = new Request();
        request.setDateTimeOfCreate(LocalDateTime.of(1, 1, 1, 1, 1));
        request.setDescription("The characteristics of someone or something");
        request.setId(1L);
        request.setRequester(user);
        return request;
    }

    private Comment createComment(User user, Item item) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setId(1L);
        comment.setItem(item);
        comment.setText("Text");
        comment.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        return comment;
    }

    private Booking createBooking(User user, Item item) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking.setStatus(Booking.Status.WAITING);
        return booking;
    }
}