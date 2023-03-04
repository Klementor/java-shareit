package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;

public interface BookingService {
    BookingResponseDto addBooking(BookItemRequestDto bookItemRequestDto, Long userId);

}
