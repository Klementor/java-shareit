package ru.practicum.shareit.booking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @MockBean
    private BookingJpaRepository bookingJpaRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private ItemJpaRepository itemJpaRepository;

    @MockBean
    private UserJpaRepository userJpaRepository;

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(BookItemRequestDto, Long)}
     */
    @Test
    void testAddBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.addBooking(bookItemRequestDto, 1L));
    }

    /**
     * Method under test: {@link BookingServiceImpl#addBooking(BookItemRequestDto, Long)}
     */
    @Test
    void testAddBooking2() {
        BookItemRequestDto bookItemRequestDto = mock(BookItemRequestDto.class);
        when(bookItemRequestDto.getEnd()).thenReturn(LocalDateTime.of(0, 1, 1, 1, 1));
        when(bookItemRequestDto.getStart()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(bookItemRequestDto).setEnd((LocalDateTime) any());
        doNothing().when(bookItemRequestDto).setItemId((Long) any());
        doNothing().when(bookItemRequestDto).setStart((LocalDateTime) any());
        bookItemRequestDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.addBooking(bookItemRequestDto, 1L));
        verify(bookItemRequestDto).getEnd();
        verify(bookItemRequestDto).getStart();
        verify(bookItemRequestDto).setEnd((LocalDateTime) any());
        verify(bookItemRequestDto).setItemId((Long) any());
        verify(bookItemRequestDto).setStart((LocalDateTime) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBooking(Long, Long, Boolean)}
     */
    @Test
    void testUpdateBooking() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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
        when(bookingJpaRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        BookingResponseDto actualUpdateBookingResult = bookingServiceImpl.updateBooking(1L, 1L, true);
        assertEquals(Booking.Status.WAITING, actualUpdateBookingResult.getStatus());
        assertEquals("01:01", actualUpdateBookingResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualUpdateBookingResult.getStart().toLocalTime().toString());
        assertEquals(1L, actualUpdateBookingResult.getId().longValue());
        BookingResponseDto.ItemDto item2 = actualUpdateBookingResult.getItem();
        assertEquals("Name", item2.getName());
        assertEquals(1L, item2.getId().longValue());
        assertEquals(1L, actualUpdateBookingResult.getBooker().getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
        verify(bookingJpaRepository).save((Booking) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBooking(Long, Long, Boolean)}
     */
    @Test
    void testUpdateBooking2() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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
        when(bookingJpaRepository.save((Booking) any())).thenThrow(new BadRequestException("An error occurred"));
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.updateBooking(1L, 1L, true));
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
        verify(bookingJpaRepository).save((Booking) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBooking(Long, Long, Boolean)}
     */
    @Test
    void testUpdateBooking3() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

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
        when(bookingJpaRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.updateBooking(1L, 1L, true));
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#updateBooking(Long, Long, Boolean)}
     */
    @Test
    void testUpdateBooking4() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

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
        Booking booking1 = mock(Booking.class);
        when(booking1.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking1.getItem()).thenReturn(item2);
        when(booking1.getId()).thenReturn(1L);
        when(booking1.getEnd()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getStart()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking1.getBooker()).thenReturn(user6);
        doNothing().when(booking1).setBooker((User) any());
        doNothing().when(booking1).setEnd((LocalDateTime) any());
        doNothing().when(booking1).setId((Long) any());
        doNothing().when(booking1).setItem((Item) any());
        doNothing().when(booking1).setStart((LocalDateTime) any());
        doNothing().when(booking1).setStatus((Booking.Status) any());
        booking1.setBooker(user3);
        booking1.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        booking1.setStatus(Booking.Status.WAITING);
        when(bookingJpaRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        BookingResponseDto actualUpdateBookingResult = bookingServiceImpl.updateBooking(1L, 1L, true);
        assertEquals(Booking.Status.WAITING, actualUpdateBookingResult.getStatus());
        assertEquals("01:01", actualUpdateBookingResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualUpdateBookingResult.getStart().toLocalTime().toString());
        assertEquals(1L, actualUpdateBookingResult.getId().longValue());
        BookingResponseDto.ItemDto item3 = actualUpdateBookingResult.getItem();
        assertEquals("Name", item3.getName());
        assertEquals(1L, item3.getId().longValue());
        assertEquals(1L, actualUpdateBookingResult.getBooker().getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
        verify(bookingJpaRepository).save((Booking) any());
        verify(booking1).getId();
        verify(booking1).getEnd();
        verify(booking1).getStart();
        verify(booking1).getStatus();
        verify(booking1).getItem();
        verify(booking1).getBooker();
        verify(booking1).setBooker((User) any());
        verify(booking1).setEnd((LocalDateTime) any());
        verify(booking1).setId((Long) any());
        verify(booking1).setItem((Item) any());
        verify(booking1).setStart((LocalDateTime) any());
        verify(booking1).setStatus((Booking.Status) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingOnlyForOwnerOrBooker() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        BookingResponseDto actualBookingOnlyForOwnerOrBooker = bookingServiceImpl.getBookingOnlyForOwnerOrBooker(1L, 1L);
        assertEquals(Booking.Status.WAITING, actualBookingOnlyForOwnerOrBooker.getStatus());
        assertEquals("01:01", actualBookingOnlyForOwnerOrBooker.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualBookingOnlyForOwnerOrBooker.getStart().toLocalTime().toString());
        assertEquals(1L, actualBookingOnlyForOwnerOrBooker.getId().longValue());
        BookingResponseDto.ItemDto item1 = actualBookingOnlyForOwnerOrBooker.getItem();
        assertEquals("Name", item1.getName());
        assertEquals(1L, item1.getId().longValue());
        assertEquals(1L, actualBookingOnlyForOwnerOrBooker.getBooker().getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingOnlyForOwnerOrBooker2() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById((Long) any())).thenThrow(new BadRequestException("An error occurred"));
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingOnlyForOwnerOrBooker(1L, 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingOnlyForOwnerOrBooker3() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);

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
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBookingOnlyForOwnerOrBooker(1L, 1L));
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingOnlyForOwnerOrBooker(Long, Long)}
     */
    @Test
    void testGetBookingOnlyForOwnerOrBooker4() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);

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
        Booking booking = mock(Booking.class);
        when(booking.getStatus()).thenReturn(Booking.Status.WAITING);
        when(booking.getId()).thenReturn(1L);
        when(booking.getEnd()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getStart()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        when(booking.getItem()).thenReturn(item1);
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
        when(bookingJpaRepository.getReferenceById((Long) any())).thenReturn(booking);
        when(bookingJpaRepository.existsById((Long) any())).thenReturn(true);
        BookingResponseDto actualBookingOnlyForOwnerOrBooker = bookingServiceImpl.getBookingOnlyForOwnerOrBooker(1L, 1L);
        assertEquals(Booking.Status.WAITING, actualBookingOnlyForOwnerOrBooker.getStatus());
        assertEquals("01:01", actualBookingOnlyForOwnerOrBooker.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualBookingOnlyForOwnerOrBooker.getStart().toLocalTime().toString());
        assertEquals(1L, actualBookingOnlyForOwnerOrBooker.getId().longValue());
        BookingResponseDto.ItemDto item2 = actualBookingOnlyForOwnerOrBooker.getItem();
        assertEquals("Name", item2.getName());
        assertEquals(1L, item2.getId().longValue());
        assertEquals(1L, actualBookingOnlyForOwnerOrBooker.getBooker().getId().longValue());
        verify(userJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).existsById((Long) any());
        verify(bookingJpaRepository).getReferenceById((Long) any());
        verify(booking, atLeast(1)).getId();
        verify(booking).getEnd();
        verify(booking).getStart();
        verify(booking).getStatus();
        verify(booking).getItem();
        verify(booking, atLeast(1)).getBooker();
        verify(booking).setBooker((User) any());
        verify(booking).setEnd((LocalDateTime) any());
        verify(booking).setId((Long) any());
        verify(booking).setItem((Item) any());
        verify(booking).setStart((LocalDateTime) any());
        verify(booking).setStatus((Booking.Status) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByUserId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetBookingsByUserId() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingsByUserId(1L, "MD", 1, 3));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByUserId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetBookingsByUserId2() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBookingsByUserId(1L, "MD", 1, 3));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByUserId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetBookingsByUserId4() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ArithmeticException.class, () -> bookingServiceImpl.getBookingsByUserId(1L, "MD", 1, 0));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByUserId(Long, String, Integer, Integer)}
     */
    @Test
    void testGetBookingsByUserId5() {
        when(userJpaRepository.existsById((Long) any())).thenThrow(new BadRequestException("An error occurred"));
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingsByUserId(1L, "MD", 1, 3));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByOwnerId(Integer, Integer, Long, String)}
     */
    @Test
    void testGetBookingsByOwnerId() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingsByOwnerId(1, 3, 1L, "MD"));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByOwnerId(Integer, Integer, Long, String)}
     */
    @Test
    void testGetBookingsByOwnerId2() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.getBookingsByOwnerId(1, 3, 1L, "MD"));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByOwnerId(Integer, Integer, Long, String)}
     */
    @Test
    void testGetBookingsByOwnerId3() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingsByOwnerId(0, 3, 1L, "MD"));
        verify(userJpaRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByOwnerId(Integer, Integer, Long, String)}
     */
    @Test
    void testGetBookingsByOwnerId5() {
        when(userJpaRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ArithmeticException.class, () -> bookingServiceImpl.getBookingsByOwnerId(1, 0, 1L, "MD"));
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingsByOwnerId(Integer, Integer, Long, String)}
     */
    @Test
    void testGetBookingsByOwnerId6() {
        when(userJpaRepository.existsById((Long) any())).thenThrow(new BadRequestException("An error occurred"));
        assertThrows(BadRequestException.class, () -> bookingServiceImpl.getBookingsByOwnerId(1, 3, 1L, "MD"));
        verify(userJpaRepository).existsById((Long) any());
    }
}

