package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(BookItemRequestDto bookItemRequestDto, Long userId);

    BookingResponseDto updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getBookingOnlyForOwnerOrBooker(Long userId, Long bookingId);

    List<BookingResponseDto> getBookingsByUserId(Long userId, String state, Integer from, Integer size);

    Iterable<BookingResponseDto> getBookingsByOwnerId(Integer from, Integer size, Long userId, String state);

    enum State {
        ALL,
        CURRENT,
        PAST,
        FUTURE,
        WAITING,
        REJECTED
    }
}
