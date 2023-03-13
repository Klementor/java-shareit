package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingServiceImpl;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto addBooking(@Valid @RequestBody BookItemRequestDto itemRequestDto,
                                         @RequestHeader(header) Long userId) {
        return bookingServiceImpl.addBooking(itemRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(@RequestHeader(header) Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam Boolean approved) {
        return bookingServiceImpl.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingOnlyForOwnerOrBooker(@PathVariable Long bookingId,
                                                             @RequestHeader(header) Long userId) {
        return bookingServiceImpl.getBookingOnlyForOwnerOrBooker(userId, bookingId);
    }

    @GetMapping
    public Iterable<BookingResponseDto> getBookingsByUserId(
            @RequestHeader(header) Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingServiceImpl.getBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public Iterable<BookingResponseDto> getBookingsByOwnerId(
            @RequestHeader(header) Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingServiceImpl.getBookingsByOwnerId(userId, state);
    }
}