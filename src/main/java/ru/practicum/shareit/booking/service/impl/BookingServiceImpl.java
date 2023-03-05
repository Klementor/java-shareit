package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final ItemJpaRepository itemJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;

    @Override
    @Transactional
    public BookingResponseDto addBooking(BookItemRequestDto bookItemRequestDto, Long userId) {
        if (bookItemRequestDto.getStart().isAfter(bookItemRequestDto.getEnd())) {
            throw new BadRequestException("Неверно указанное время");
        }
        if (bookItemRequestDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Время старта указано неверно");
        }
        if (!itemJpaRepository.existsById(bookItemRequestDto.getItemId()) || !userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя либо вещи не существует");
        }

        Item item = itemJpaRepository.getReferenceById(bookItemRequestDto.getItemId());
        User user = userJpaRepository.getReferenceById(userId);
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        Booking booking = bookingJpaRepository.save(BookingMapper.toBooking(bookItemRequestDto, user, item));

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBooking(Long userId, Long bookingId, Boolean approved) {

        if (!bookingJpaRepository.existsById(bookingId) || !userJpaRepository.existsById(userId)) {
            throw new BadRequestException("Booking id или user id неверные");
        }
        Booking booking = bookingJpaRepository.getReferenceById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BadRequestException("Указанный user id не является идентификатором владельца вещи");
        }
        if (booking.getStatus() == Booking.Status.APPROVED) {
            throw new BadRequestException("Статус approved уже установлен");
        }

        Booking.Status status = approved ? (Booking.Status.APPROVED) : (Booking.Status.REJECTED);
        booking.setStatus(status);
        Booking updatedBooking = bookingJpaRepository.save(booking);
        return BookingMapper.toBookingResponseDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingOnlyForOwnerOrBooker(Long userId, Long bookingId) {
        if (!bookingJpaRepository.existsById(bookingId) || !userJpaRepository.existsById(userId)) {
            throw new BadRequestException("Booking id или user id неверные");
        }
        Booking booking = bookingJpaRepository.getReferenceById(bookingId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new BadRequestException("Указанное user id не является id владельца или заказчика");
        }
        log.info("Отправлены данные о booking с id = {}", booking.getId());
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId, String state) {
        if (!userJpaRepository.existsById(userId)) {
            throw new NotFoundException("Такого user id не существует");
        }
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                bookings = bookingJpaRepository.findAllByBookerId(userId, Sort.by("start").descending());
                break;
            case "CURRENT":
                bookings = bookingJpaRepository.findCurrentBookingByBookerId(userId, now);
                break;
            case "PAST":
                bookings = bookingJpaRepository.findPastBookingByBookerId(userId, now);
                break;
            case "FUTURE":
                bookings = bookingJpaRepository.findFutureBookingByBookerId(userId,
                        now,
                        Sort.by("start").descending());
                break;
            case "WAITING":
                bookings = bookingJpaRepository.findBookingByBookerIdAndStatus(userId, Booking.Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingJpaRepository.findBookingByBookerIdAndStatus(userId, Booking.Status.REJECTED);
                break;
            default:
                throw new BadRequestException(String.format("Unknown state: %s", state));
        }
        return BookingMapper.toListBookingResponseDto(bookings);
    }
}
