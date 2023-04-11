package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.validation.annotation.BookingStateEnum;
import ru.practicum.shareit.constants.HttpHeadersConstants;
import ru.practicum.shareit.validation.group.CreationGroup;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public BookingResponseDto addBooking(
            @RequestBody @Validated(CreationGroup.class) BookItemRequestDto bookingDto,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBookerId(
            @RequestParam(defaultValue = "ALL") @BookingStateEnum String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getAllByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByBookerItems(
            @RequestParam(defaultValue = "ALL") @BookingStateEnum String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestHeader(HttpHeadersConstants.X_SHARER_USER_ID) Long userId
    ) {
        return bookingClient.getAllByBookerItems(userId, state, from, size);
    }
}
