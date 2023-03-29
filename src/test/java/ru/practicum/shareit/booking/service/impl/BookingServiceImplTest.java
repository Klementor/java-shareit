package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private ItemJpaRepository itemJpaRepository;
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private BookingJpaRepository bookingJpaRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("should return all bookings for the given user id")
    void getBookingsByUserIdForAllState() {
        Long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        List<Booking> bookings = Collections.singletonList(booking);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(bookingJpaRepository.findBookingByBookerId(any(), any())).thenReturn(bookings);

        List<BookingResponseDto> actualBookings =
                bookingService.getBookingsByUserId(userId, state, from, size);

        assertEquals(bookings.get(0).getId(), actualBookings.get(0).getId());
    }

    @Test
    @DisplayName("should return future bookings for the given user id")
    void getBookingsByUserIdForFutureState() {
        Long userId = 1L;
        String state = "FUTURE";
        Integer from = 0;
        Integer size = 10;

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user.setId(2L);
        user.setName("user2");
        user.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Booking.Status.WAITING);
        List<Booking> bookings = Collections.singletonList(booking);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(bookingJpaRepository.findBookingByBookerIdAndStartIsAfter(any(), any(), any()))
                .thenReturn(bookings);
        List<BookingResponseDto> result = bookingService.getBookingsByUserId(userId, state, from, size);

        assertEquals(bookings.get(0).getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("should return waiting bookings for the given user id")
    void getBookingsByUserIdForWaitingState() {
        Long userId = 1L;
        String state = "WAITING";
        Integer from = 0;
        Integer size = 10;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Booking.Status.WAITING);
        List<Booking> bookings = Collections.singletonList(booking);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(bookingJpaRepository.findBookingByBookerIdAndStatusEquals(
                userId, Booking.Status.WAITING, PageRequest.of(from / size, size)))
                .thenReturn(bookings);
        List<BookingResponseDto> result =
                bookingService.getBookingsByUserId(userId, state, from, size);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("should return past bookings for the given user id")
    void getBookingsByUserIdForPastState() {
        Long userId = 1L;
        String state = "PAST";
        Integer from = 0;
        Integer size = 10;
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(now.minusDays(1));
        booking.setEnd(now.minusDays(1));
        List<Booking> bookings = Collections.singletonList(booking);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(bookingJpaRepository.findBookingByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(
                userId, now, now, PageRequest.of(from / size, size)))
                .thenReturn(bookings);
        List<BookingResponseDto> result =
                bookingService.getBookingsByUserId(userId, state, from, size);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("should return current bookings for the given user id")
    void getBookingsByUserIdForCurrentState() {
        Long userId = 1L;
        String state = "CURRENT";
        Integer from = 0;
        Integer size = 10;
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user.setId(2L);
        user.setName("user2");
        user.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(now.minusDays(1));
        booking1.setEnd(now.plusDays(1));
        booking1.setItem(item);
        booking1.setBooker(user2);
        List<Booking> bookings = Arrays.asList(booking1);
        when(userJpaRepository.existsById(userId)).thenReturn(true);
        when(bookingJpaRepository.findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
                any(), any(), any(), any()))
                .thenReturn(bookings);
        List<BookingResponseDto> result =
                bookingService.getBookingsByUserId(userId, state, from, size);
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("should return waiting bookings for the owner when state is WAITING")
    void getBookingsByOwnerIdWhenStateIsWaiting() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");
        userJpaRepository.save(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        itemJpaRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Booking.Status.WAITING);
        bookingJpaRepository.save(booking);

        when(userJpaRepository.existsById(any())).thenReturn(true);
        Iterable<BookingResponseDto> bookings =
                bookingService.getBookingsByOwnerId(0, 10, 1L, "WAITING");

        assertEquals(1, List.of(bookings).size());
    }

    @Test
    @DisplayName("should return future bookings for the owner when state is FUTURE")
    void getBookingsByOwnerIdWhenStateIsFuture() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");
        userJpaRepository.save(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        itemJpaRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        bookingJpaRepository.save(booking);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        Iterable<BookingResponseDto> bookings =
                bookingService.getBookingsByOwnerId(0, 10, 1L, "FUTURE");

        assertEquals(1, List.of(bookings).size());
    }

    @Test
    @DisplayName("should return all bookings for the owner when state is ALL")
    void getBookingsByOwnerIdWhenStateIsAll() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");
        userJpaRepository.save(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        itemJpaRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        bookingJpaRepository.save(booking);

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setBooker(BookingResponseDto.UserDto.fromUser(booking.getBooker()));
        bookingResponseDto.setItem(BookingResponseDto.ItemDto.fromItem(booking.getItem()));
        bookingResponseDto.setStatus(booking.getStatus());

        when(userJpaRepository.existsById(any())).thenReturn(true);
        Iterable<BookingResponseDto> bookings = bookingService.getBookingsByOwnerId(0, 10, user.getId(), "ALL");
        assertEquals(
                1,
                List.of(bookings).size());
    }

    @Test
    @DisplayName("should return past bookings for the owner when state is PAST")
    void getBookingsByOwnerIdWhenStateIsPast() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");
        userJpaRepository.save(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        itemJpaRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Booking.Status.APPROVED);
        bookingJpaRepository.save(booking);

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setBooker(BookingResponseDto.UserDto.fromUser(booking.getBooker()));
        bookingResponseDto.setItem(BookingResponseDto.ItemDto.fromItem(booking.getItem()));
        bookingResponseDto.setStatus(booking.getStatus());

        when(userJpaRepository.existsById(any())).thenReturn(true);
        Iterable<BookingResponseDto> bookings = bookingService.getBookingsByOwnerId(0, 10, user.getId(), "PAST");
        assertEquals(
                1,
                List.of(bookings).size());
    }

    @Test
    @DisplayName("should return current bookings for the owner when state is CURRENT")
    void getBookingsByOwnerIdWhenStateIsCurrent() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");
        userJpaRepository.save(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        itemJpaRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Booking.Status.APPROVED);
        bookingJpaRepository.save(booking);
        when(userJpaRepository.existsById(any())).thenReturn(true);

        Iterable<BookingResponseDto> bookings =
                bookingService
                        .getBookingsByOwnerId(0, 10, user.getId(), "CURRENT");

        assertEquals(
                1,
                List.of(bookings).size());
    }

    @Test
    @DisplayName("should throw BadRequestException when start time is equal to end time")
    void addBookingWhenStartTimeIsEqualToEndTimeThenThrowBadRequestException() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setStart(LocalDateTime.now());
        bookItemRequestDto.setEnd(LocalDateTime.now());
        bookItemRequestDto.setItemId(1L);
        Long userId = 1L;

        assertThrows(
                BadRequestException.class,
                () -> bookingService.addBooking(bookItemRequestDto, userId));
    }

    @Test
    @DisplayName("should throw BadRequestException when start time is after end time")
    void addBookingWhenStartTimeIsAfterEndTimeThenThrowBadRequestException() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now());
        bookItemRequestDto.setItemId(1L);
        Long userId = 1L;

        assertThrows(
                BadRequestException.class,
                () -> bookingService.addBooking(bookItemRequestDto, userId));
    }

    @Test
    @DisplayName("should throw BadRequestException when start time is before current time")
    void addBookingWhenStartTimeIsBeforeCurrentTimeThenThrowBadRequestException() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setStart(LocalDateTime.now().minusDays(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(1));
        bookItemRequestDto.setItemId(1L);
        Long userId = 1L;

        assertThrows(
                BadRequestException.class,
                () -> bookingService.addBooking(bookItemRequestDto, userId));
    }

    @Test
    @DisplayName("should throw NotFoundException when item or user does not exist")
    void addBookingWhenItemOrUserDoesNotExistThenThrowNotFoundException() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(1));
        Long userId = 1L;
        when(itemJpaRepository.existsById(bookItemRequestDto.getItemId())).thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> bookingService.addBooking(bookItemRequestDto, userId));
    }

    @Test
    @DisplayName("should add booking when all input parameters are valid")
    void addBookingWhenAllInputParametersAreValid() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookItemRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Booking.Status.APPROVED);

        when(itemJpaRepository.getReferenceById(any())).thenReturn(item);
        when(bookingJpaRepository.save(any())).thenReturn(booking);
        when(itemJpaRepository.existsById(any())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        BookingResponseDto bookingResponseDto =
                bookingService.addBooking(bookItemRequestDto, user2.getId());
        assertEquals(booking.getId(), bookingResponseDto.getId());
    }

    @Test
    @DisplayName("should throw a NotFoundException when the booking id or user id is invalid")
    void updateBookingWhenBookingIdOrUserIdIsInvalidThenThrowNotFoundException() {
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingJpaRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> bookingService.updateBooking(userId, bookingId, true));
    }

    @Test
    @DisplayName("should throw a BadRequestException when the booking status is already approved")
    void updateBookingWhenStatusIsAlreadyApprovedThenThrowBadRequestException() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Booking.Status.APPROVED);

        when(bookingJpaRepository.getReferenceById(booking.getId())).thenReturn(booking);
        when(bookingJpaRepository.existsById(booking.getId())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> bookingService.updateBooking(user.getId(), booking.getId(), true));
    }

    @Test
    @DisplayName("should throw a NotFoundException when the user id is not the owner of the item")
    void updateBookingWhenUserIdIsNotOwnerThenThrowNotFoundException() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Booking.Status.APPROVED);

        when(userJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById(any())).thenReturn(booking);

        assertThrows(
                NotFoundException.class,
                () -> bookingService.updateBooking(3L, booking.getId(), true));
    }

    @Test
    @DisplayName(
            "should update the booking status to approved when the user is the owner and the status is not already approved")
    void updateBookingToApprovedWhenUserIsOwnerAndStatusNotApproved() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);

        when(bookingJpaRepository.existsById(any())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById(any())).thenReturn(booking);
        when(bookingJpaRepository.save(booking)).thenReturn(booking);

        BookingResponseDto bookingResponseDto =
                bookingService.updateBooking(user.getId(), booking.getId(), true);

        assertEquals(Booking.Status.APPROVED, bookingResponseDto.getStatus());
    }

    @Test
    @DisplayName(
            "should update the booking status to rejected when the user is the owner and the status is not already approved")
    void updateBookingToRejectedWhenUserIsOwnerAndStatusNotApproved() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);

        when(bookingJpaRepository.existsById(any())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById(any())).thenReturn(booking);
        when(bookingJpaRepository.save(booking)).thenReturn(booking);

        BookingResponseDto bookingResponseDto =
                bookingService.updateBooking(user.getId(), booking.getId(), false);

        assertEquals(Booking.Status.REJECTED, bookingResponseDto.getStatus());
    }

    @Test
    @DisplayName("should throw a NotFoundException when the booking id or user id is invalid")
    void getBookingOnlyForOwnerOrBookerWhenInvalidIdsThenThrowNotFoundException() {
        when(bookingJpaRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingOnlyForOwnerOrBooker(1L, 1L));
    }

    @Test
    @DisplayName(
            "should throw a NotFoundException when the user is neither the owner nor the booker")
    void getBookingOnlyForOwnerOrBookerWhenUserIsNotOwnerOrBookerThenThrowNotFoundException() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);

        when(bookingJpaRepository.existsById(any())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById(any())).thenReturn(booking);
        assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingOnlyForOwnerOrBooker(3L, booking.getId()));
    }

    @Test
    @DisplayName("should return the booking when the user is the owner or the booker")
    void getBookingOnlyForOwnerOrBookerWhenUserIsOwnerOrBooker() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);

        when(bookingJpaRepository.existsById(any())).thenReturn(true);
        when(userJpaRepository.existsById(any())).thenReturn(true);
        when(bookingJpaRepository.getReferenceById(any())).thenReturn(booking);
        BookingResponseDto bookingResponseDto =
                bookingService.getBookingOnlyForOwnerOrBooker(user.getId(), booking.getId());
        assertEquals(booking.getId(), bookingResponseDto.getId());
    }
}