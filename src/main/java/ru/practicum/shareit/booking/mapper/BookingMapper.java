package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {

    public static Booking toBooking(BookItemRequestDto bookItemRequestDto, User user, Item item) {
        Booking booking = new Booking();

        booking.setId(bookItemRequestDto.getItemId());
        booking.setStart(bookItemRequestDto.getStart());
        booking.setEnd(bookItemRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);

        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setBooker(BookingResponseDto.UserDto.fromUser(booking.getBooker()));
        bookingResponseDto.setItem(BookingResponseDto.ItemDto.fromItem(booking.getItem()));
        bookingResponseDto.setStatus(booking.getStatus());

        return bookingResponseDto;
    }
}
