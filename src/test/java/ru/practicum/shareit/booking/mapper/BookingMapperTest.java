package ru.practicum.shareit.booking.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingMapperTest {
    /**
     * Method under test: {@link BookingMapper#toBooking(BookItemRequestDto, User, Item)}
     */
    @Test
    void testToBooking() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto();
        bookItemRequestDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookItemRequestDto.setItemId(1L);
        bookItemRequestDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));

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
        Booking actualToBookingResult = BookingMapper.toBooking(bookItemRequestDto, user, item);
        assertSame(user, actualToBookingResult.getBooker());
        assertEquals(Booking.Status.WAITING, actualToBookingResult.getStatus());
        assertSame(item, actualToBookingResult.getItem());
        assertEquals("01:01", actualToBookingResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualToBookingResult.getStart().toLocalTime().toString());
    }

    /**
     * Method under test: {@link BookingMapper#toBookingResponseDto(Booking)}
     */
    @Test
    void testToBookingResponseDto() {
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
        BookingResponseDto actualToBookingResponseDtoResult = BookingMapper.toBookingResponseDto(booking);
        assertEquals(Booking.Status.WAITING, actualToBookingResponseDtoResult.getStatus());
        assertEquals("01:01", actualToBookingResponseDtoResult.getEnd().toLocalTime().toString());
        assertEquals("01:01", actualToBookingResponseDtoResult.getStart().toLocalTime().toString());
        assertEquals(1L, actualToBookingResponseDtoResult.getId().longValue());
        BookingResponseDto.ItemDto item1 = actualToBookingResponseDtoResult.getItem();
        assertEquals("Name", item1.getName());
        assertEquals(1L, item1.getId().longValue());
        assertEquals(1L, actualToBookingResponseDtoResult.getBooker().getId().longValue());
    }

    /**
     * Method under test: {@link BookingMapper#toListBookingResponseDto(Iterable)}
     */
    @Test
    void testToListBookingResponseDto() {
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

        LinkedHashSet<Booking> bookingSet = new LinkedHashSet<>();
        bookingSet.add(booking);
        assertEquals(1, BookingMapper.toListBookingResponseDto(bookingSet).size());
    }

    /**
     * Method under test: {@link BookingMapper#toListBookingResponseDto(Iterable)}
     */
    @Test
    void testToListBookingResponseDto2() {
        Iterable<Booking> iterable = (Iterable<Booking>) mock(Iterable.class);
        when(iterable.iterator()).thenReturn(MappingIterator.emptyIterator());
        assertTrue(BookingMapper.toListBookingResponseDto(iterable).isEmpty());
        verify(iterable).iterator();
    }
}

