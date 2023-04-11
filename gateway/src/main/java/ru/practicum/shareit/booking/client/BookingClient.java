package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.constants.HttpHeadersConstants;

import java.util.List;

@Service
public class BookingClient {
    private final WebClient client;

    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public BookingResponseDto addBooking(BookItemRequestDto bookingDto, Long userId) {
        return client.post()
                .uri("/bookings")
                .bodyValue(bookingDto)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingResponseDto.class)
                .block();
    }

    public BookingResponseDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        return client.patch()
                .uri(builder -> builder.path("/bookings/{id}")
                        .queryParam("approved", approved)
                        .build(bookingId))
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingResponseDto.class)
                .block();
    }

    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        return client.get()
                .uri("/bookings/{id}", bookingId)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingResponseDto.class)
                .block();
    }

    public List<BookingResponseDto> getAllByBookerId(Long userId, String state, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/bookings")
                        .queryParam("state", state)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(BookingResponseDto.class)
                .collectList()
                .block();

    }

    public List<BookingResponseDto> getAllByBookerItems(Long userId, String state, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/bookings/owner")
                        .queryParam("state", state)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(BookingResponseDto.class)
                .collectList()
                .block();
    }
}